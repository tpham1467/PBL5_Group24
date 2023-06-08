# Robot-waitress

# Introduction

- Robot phục vụ nhà hàng được có các chức năng chính:
  + Ghi nhận order bằng giọng nói ( sẽ được hiển thị trên app )
  + Vận chuyển món ăn
- Công nghệ sử dụng:

| Vấn đề               | Công nghệ/Giải pháp              |
| :--------------------| :--------------------------------|
| `App`                | `Android`                        | 
| `Speech recognition` | `Hideen Markov Model`            |
| `Robot`              | `Following Line by intersection` |
  
# Setup 
- Cấu trúc dự án
```
├───App
├───datasets
│   ├───1 Danh mục
│   │   └───1.1 Từ
|   |     └───1.1.1.wav
|   |     └───1.1.index.wav
│   │   └───1.2
|   |   └───1.3
|   |   └───1.Index
│   └───2
│   └───3
|   └───test chứa file âm thanh để test mô hình HMM ghép
|   └───Sentence.txt Mô tả các từ trong mô hình ghép
|   └───Sentence_test.txt Đường dẫn các file âm thanh và nhãn ( Mô hình HMM ghép)(
|   └───word.txt Danh sách từ đơn : Name,State,Category
├───HMM
├───Robot
```

- Các bước để chạy dự án:
 + Chuẩn bị linh kiện: tham khảo [Document](https://github.com/tpham1467/Robot-waitress/blob/main/Ba%CC%81o%20ca%CC%81o%20nho%CC%81m%2024_PBL5_Robot%20phu%CC%A3c%20vu%CC%A3%20nha%CC%80%20ha%CC%80ng.pdf)
 + Cài đặt app: [App](https://github.com/tpham1467/Robot-waitress/blob/main/App/RestaurantManager/README.md)
 + Training mô hình HMM: [HMM](https://github.com/tpham1467/Robot-waitress/blob/main/HMM/HMMSpeechRecognition/README.md)
 + Setup Robot: [Robot](https://github.com/tpham1467/Robot-waitress/blob/main/Robot/README.md)

# [Demo](https://drive.google.com/file/d/1tLvUo7U4pSyb8L2YUeqocT2AOkOef6s4/view)










