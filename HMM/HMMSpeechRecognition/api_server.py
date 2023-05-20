from flask import Flask, request
from hmm_train import *
from  working_with_firebase import Working_with_Firebase,firebase_url
app = Flask(__name__)

@app.route('/api/predict_wavfile', methods=['POST'])
def process_audio():
    file = request.files['file']
    file.save(dst=r'temp/test.wav')

    try:
        predict = gmm.predict_file(file)
    except:
        return 'Không Nhận diện được câu trả lời'
    print(predict)
    return predict

@app.route('/api/add_new_Request/<string:id_table>', methods=['POST'])
def add_food_to_table(id_table):
    content = request.form['content_request'].lower()
    app.logger.info('Id Table |' + id_table + '|')
    app.logger.info('Yêu cầu |' + content + '|')
    kq = Working1.textToRequest(id_table,content)

    if kq is None:
        app.logger.info('Error')
    else:
        app.logger.info('Kết quả :' + kq)
        return kq
if __name__ == '__main__':
    gmm = GMM(words)
    with open('models_train/model.pkl', 'rb') as f:
        gmm = pickle.load(f)
    import socket
    host = socket.gethostbyname(socket.gethostname())

    Working1 = Working_with_Firebase(firebase_url)
    app.run(debug=True,host=host)