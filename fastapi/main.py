from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from ultralytics import YOLO
from pydantic import BaseModel
import cv2
import numpy as np
from PIL import Image
import io
from transformers import AutoTokenizer, MarianMTModel, pipeline

app = FastAPI(title="e-commerce API")

model = YOLO('./weapon_best.pt')

ko_en_model_name = "Helsinki-NLP/opus-mt-ko-en"
ko_en_tokenizer = AutoTokenizer.from_pretrained(ko_en_model_name)
ko_en_model = MarianMTModel.from_pretrained(ko_en_model_name)

# en-ko 번역 모델
en_ko_translator = pipeline('translation', 
                          model='facebook/nllb-200-distilled-600M', 
                          device=-1,  # GPU 사용. CPU만 사용시 device=-1로 변경
                          src_lang='eng_Latn',
                          tgt_lang='kor_Hang',
                          max_length=512)

class TranslationRequest(BaseModel):
    text: str

@app.post("/ko-en/")
async def translate_korean_to_english(request: TranslationRequest):
    try:
        inputs = ko_en_tokenizer(request.text, return_tensors="pt", padding=True)
        
        translated = ko_en_model.generate(**inputs)
        
        result = ko_en_tokenizer.decode(translated[0], skip_special_tokens=True)
        
        return JSONResponse({
            "korean": request.text,
            "english": result
        })
        
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Translation error: {str(e)}"
        )

@app.post("/en-ko/")
async def translate_english_to_korean(request: TranslationRequest):
    try:
        # NLLB 모델
        output = en_ko_translator(request.text, max_length=512)
        result = output[0]['translation_text']
        
        return JSONResponse({
            "english": request.text,
            "korean": result
        })
        
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Translation error: {str(e)}"
        )

class DetectionRequest(BaseModel):
    path: str

@app.post("/image_validate")
#async def detect_weapons(file: UploadFile = File(...)):
async def detect_weapons(request: DetectionRequest):
    try:
        image = cv2.imread(request.path)
        if image is None:
            raise HTTPException(
                status_code=400,
                detail= "path not valid"
            )
       
        results = model.predict(source=image, conf=0.5)
        
        # 결과 처리
        detections = []
        
        # results[0] -> 박스 정보 추출
        if len(results[0].boxes) > 0:  # 탐지된 객체가 있는 경우
            boxes = results[0].boxes
            for box in boxes:
                detection = {
                    "bbox": box.xyxy[0].tolist(),
                    "confidence": float(box.conf[0]),
                    "class_id": int(box.cls[0])
                }
                detections.append(detection)
            
            return JSONResponse({
                "detected": True,
                "detections": detections
            })
        
        # 탐지된 객체가 없는 경우
        return JSONResponse({
            "detected": False,
            "detections": []
        })
        
    except Exception as e:
        return JSONResponse({
            "error": str(e)
        }, status_code=500)

@app.get("/")
async def root():
    return {"message": "Weapon Detection API",
            "endpoints": {
                    "weapon_detection": "/detect/",
                    "korean_to_english": "/ko-en/",
                    "english_to_korean": "/en-ko/"
                }
            }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

'''
from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
import numpy as np
import cv2
from ultralytics import YOLO
import io
from PIL import Image

app = FastAPI(title="Weapon Detection API")

# YOLOv8 모델 로드
model = YOLO('weapon_best.pt')

def process_image(image_bytes):
    # 바이트 스트림을 PIL Image로 변환
    image = Image.open(io.BytesIO(image_bytes))
    
    # 이미지에서 객체 감지 수행
    results = model(image)
    
    # 결과 처리
    detections = []
    for result in results:
        boxes = result.boxes
        for box in boxes:
            # 바운딩 박스 좌표
            x1, y1, x2, y2 = box.xyxy[0].tolist()
            # 신뢰도 점수
            confidence = float(box.conf[0])
            # 클래스 ID
            class_id = int(box.cls[0])
            
            detections.append({
                "bbox": [x1, y1, x2, y2],
                "confidence": confidence,
                "class_id": class_id,
                "class_name": class_name
            })
    
    return detections

@app.post("/detect/")
async def detect_weapons(file: UploadFile = File(...)):
    try:
        # 파일 읽기
        contents = await file.read()
        
        # 이미지 처리 및 감지 수행
        detections = process_image(contents)
        
        # 무기가 감지되었는지 여부 확인
        weapons_detected = len(detections) > 0
        
        return JSONResponse({
            "weapons_detected": weapons_detected,
            "detections": detections,
            "message": "Weapons detected!" if weapons_detected else "No weapons detected."
        })
        
    except Exception as e:
        return JSONResponse({
            "error": str(e)
        }, status_code=500)

@app.get("/")
async def root():
    return {"message": "Welcome to Weapon Detection API"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
'''