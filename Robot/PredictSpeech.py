from mic import record
from tex2speech import Text2Speech



def predict_wavfile(idTable):
    Text2Speech("Bắt đầu nói!")
    record()

    import requests

    url = 'http://192.168.43.212:5000/api/predict_wavfile'
    file = {'file': open('temp.wav', 'rb' )}

    response = requests.post(url, files=file )

    if response.status_code == 200:
        url = 'http://192.168.43.212:5000/api/add_new_Request/' + idTable
        print(response.text)
        if response.text == 'Không Nhận diện được câu trả lời':
            Text2Speech('Không Nhận diện được câu trả lời')
        elif 'Cần' in response.text:

            payload = {'content_equest':response.text}
            response1 = requests.post(url, data=payload )
            Text2Speech(response1.text)
            print('Yêu cầu ' +  response.text + ' đã được ghi nhận')
        elif 'Hủy' in response.text:
            payload = {'content_equest':response.text}
            response1 = requests.post(url, data=payload )
            Text2Speech(response1.text)
            print('Bạn vừa hủy :' + response.text.replace('Hủy','') )
        else:
            payload = {'content_equest':response.text}
            response1 = requests.post(url, data=payload )
            Text2Speech(response1.text)
            print('Bạn vừa gọi :' + response.text.replace('Thêm' , '') )
