import math

import librosa
import numpy as np
from pydub import AudioSegment
import noisereduce as nr

def detect_leading_silence(sound, silence_threshold=-40.0, chunk_size=30):
    trim_ms = 0  # ms

    assert chunk_size > 0  # to avoid infinite loop
    while sound[trim_ms:trim_ms + chunk_size].dBFS < silence_threshold and trim_ms < len(sound):
        # print(trim_ms)
        trim_ms += chunk_size

    return trim_ms

def get_mfcc(file_path):
    # print(file_path)

    y, sr = librosa.load(file_path)  # read .wav file
    # reduced_noise = nr.reduce_noise(y=y,
    #                                 sr=sr,
    #                                 prop_decrease=1,
    #                                 n_jobs=-1
    #                                 )
    # from scipy.io import wavfile
    # wavfile.write(r'temp/trimmed.wav', sr, reduced_noise)

    # sound = AudioSegment.from_file(r'temp/trimmed.wav', format='wav')
    #
    # start_trim = detect_leading_silence(sound)
    # end_trim = detect_leading_silence(sound.reverse())
    #
    # duration = len(sound)
    #
    # trimmed_sound = sound[start_trim:duration - end_trim]
    # trimmed_sound.export('temp/trimmed.wav', format='wav')

    # y, sr = librosa.load('temp/trimmed.wav')

    hop_length = math.floor(sr * 0.010)  # 10ms hop
    win_length = math.floor(sr * 0.025)  # 25ms frame
    # mfcc is 12 x T matrix
    mfcc = librosa.feature.mfcc(
        y=y, sr=sr, n_mfcc=13
        , n_fft=1024,
        hop_length=hop_length, win_length=win_length)
    # subtract mean from mfcc --> normalize mfcc
    # print(mfcc)
    # print(np.mean(mfcc, axis=1).reshape((-1, 1)))

    mfcc = mfcc - np.mean(mfcc, axis=1).reshape((-1, 1))

    # print(mfcc)
    # delta feature 1st order and 2nd order
    delta1 = librosa.feature.delta(mfcc, order=1)
    delta2 = librosa.feature.delta(mfcc, order=2)
    # X is 36 x T
    X = np.concatenate([mfcc, delta1, delta2], axis=0)  # O^r
    # return T x 36 (transpose of X)
    return X.T  # hmmlearn use T x N matrix

