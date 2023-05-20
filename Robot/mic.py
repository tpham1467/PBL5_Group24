import wave
import pyaudio
from pydub import AudioSegment

p = pyaudio.PyAudio()
with wave.open(r'D:\PBL5\101_HMMSpeechRecognition\temp\them1gaquay3.wav', 'rb') as wf:
    print(p.get_format_from_width(wf.getsampwidth()))



def record():
    CHUNK = 1024
    FORMAT = pyaudio.paInt16
    CHANNELS = 1
    RATE = 22050
    RECORD_SECONDS = 2

    

    stream = p.open(format=FORMAT,
                        channels=CHANNELS,
                        rate=RATE,
                        input=True,
                        frames_per_buffer=CHUNK)

    frames = []

    print('recording ...')
    for i in range(0, int(RATE / CHUNK * RECORD_SECONDS)):
        data = stream.read(CHUNK)
        frames.append(data)

    print('stopped record!')
    stream.stop_stream()
    stream.close()
    p.terminate()

    wf = wave.open('temp.wav', 'wb')
    wf.setnchannels(CHANNELS)
    wf.setsampwidth(p.get_sample_size(FORMAT))
    wf.setframerate(RATE)
    wf.writeframes(b''.join(frames))
    wf.close()

record()