import os
import pickle
import wave
import warnings
from pathlib import Path
import logging

import pandas as pandas
import seaborn as sns
from matplotlib import pyplot as plt

warnings.filterwarnings("ignore")
import pyaudio
from pydub import AudioSegment
import hmmlearn.hmm as hmm
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
import preprocessing
import pandas
import time
from sklearn import metrics
from tabulate import tabulate


class Word(object):
    def __init__(self,category,order,name,state):
        self.category = category
        self.order = order
        self.name = name
        self.state = state
    def GetPath(self):
        path1 = self.category.__str__()
        path2 = self.category.__str__() + '.' + self.order.__str__()
        import glob
        # print(glob.glob(os.path.join("Dataset",path1,path2) + '\\*.wav' ) )
        return glob.glob(os.path.join("Dataset",path1,path2  + '\\*.wav'))

    def __str__(self):
        return self.name


words = []
# Category 1
words.append(Word(1,1,'Một',8))
words.append(Word(1,2,'Hai',9))
words.append(Word(1,3,'Ba',7))
words.append(Word(1,4,'Bốn',9))
words.append(Word(1,5,'Năm',9))
words.append(Word(1,6,'Sáu',8))
# words.append(Word(1,7,'Bảy',7))
# # # words.append(Word(1,8,'Tám',7))
# # # words.append(Word(1,9,'Chín',7))
# # # words.append(Word(1,10,'Mười',7))
# #
# # # Category 2
# #
# # words.append(Word(2,1,'Gà Quay',15))
words.append(Word(2,2,'Bún Bò',14))
words.append(Word(2,3,'Mỳ Quảng',18))
words.append(Word(2,4,'Thịt Nướng',18))
# words.append(Word(2,5,'Gỏi Cá',14))
words.append(Word(2,6,'Mực Hấp',15))
# words.append(Word(2,8,'Canh Chua',15))
# words.append(Word(2,9,'Cá Hồi',15))
# words.append(Word(2,13,'Nước Cam',16))
words.append(Word(2,14,'Tương Ớt',15))
# words.append(Word(2,17,'Nước Nắm',14))
words.append(Word(2,18,'Nước Tương',16))
# words.append(Word(2,19,'Nước Suối',18))
# words.append(Word(2,20,'Rau Trộn',14))

# Category 3

words.append(Word(3,2,'Hủy',9))
words.append(Word(3,3,'Thêm',9))
words.append(Word(3,9,'Cần',9))
words.append(Word(3,10,'Xem',9))

# Category 4
words.append(Word(4,1,'Nhân Viên',15))
words.append(Word(4,7,'Menu',7))


class GMM(object):
    def __init__(self, words):
        self.X = {'train': {}, 'test': {}}
        self.y = {'train': {}, 'test': {}}
        self.model = {}
        self.all_data = {}
        self.all_labels = {}
        self.words = words

        self.sentencemodel = {}

        self.thresholds = {}
        print("Total Word :" ,len(words))

    def init_model(self):
        self._load_data()
        self._split_data()

        f = open("Dataset/Sentence.txt", "r", encoding='utf8')

        self.sentences = f.read().split('\n')


    def add_word(self,word):
        self.words.append(word)

    def _load_data(self):
        for i in self.words:
            file_paths = i.GetPath()
            data = [preprocessing.get_mfcc(file_path) for file_path in file_paths]
            self.all_data[i.__str__()] = data
            self.all_labels[i.__str__()] = [words.index(i) for _ in range(len(file_paths))]

    def _split_data(self):
        for i in self.words:
            x_train, x_test, y_train, y_test = train_test_split(
                self.all_data[i.__str__()], self.all_labels[i.__str__()],
                test_size=0.33,
                random_state=42
            )

            self.X['train'][i.__str__()] = x_train
            self.X['test'][i.__str__()] = x_test
            self.y['train'][i.__str__()] = y_train
            self.y['test'][i.__str__()] = y_test

        total_train = 0
        total_test = 0
        for i in self.words:
            train_count = len(self.X['train'][i.__str__()])
            test_count = len(self.X['test'][i.__str__()])
            print(i.__str__(), '| Train: ', train_count, '| Test: ', test_count)
            total_train += train_count
            total_test += test_count
        print('Train samples:', total_train)
        print('Test samples', total_test)

    def _init_by_bakis(self,inumstates, ibakisLevel):
        startprobPrior = np.zeros(inumstates)
        startprobPrior[0: ibakisLevel - 1] = 1 / float((ibakisLevel - 1))
        transmatPrior = self._get_transmat_prior(inumstates, ibakisLevel)
        return startprobPrior, transmatPrior

    def _get_transmat_prior(self,inumstates, ibakisLevel):
        transmatPrior = (1 / float(ibakisLevel)) * np.eye(inumstates)

        for i in range(inumstates - (ibakisLevel - 1)):
            for j in range(ibakisLevel - 1):
                transmatPrior[i, i + j + 1] = 1. / ibakisLevel

        for i in range(inumstates - ibakisLevel + 1, inumstates):
            for j in range(inumstates - i - j):
                transmatPrior[i, i + j] = 1. / (inumstates - i)

        return transmatPrior

    def _detect_leading_silence(self ,sound, silence_threshold=-20.0, chunk_size=20):
        trim_ms = 0  # ms

        assert chunk_size > 0  # to avoid infinite loop
        # print(sound[trim_ms:trim_ms + chunk_size].dBFS)
        while sound[trim_ms:trim_ms + chunk_size].dBFS < silence_threshold and trim_ms < len(sound):
            # print(sound[trim_ms:trim_ms + chunk_size].dBFS)
            trim_ms += chunk_size

        return trim_ms

    def predict_file(self,filename):
        # print(filename)
        sound = AudioSegment.from_file(filename, format='wav')

        start_trim = self._detect_leading_silence(sound)
        end_trim = self._detect_leading_silence(sound.reverse())

        duration = len(sound)

        trimmed_sound = sound[start_trim:duration - end_trim]
        trimmed_sound.export('temp/trimmed.wav', format='wav')
        # Predict
        record_mfcc = preprocessing.get_mfcc('temp/trimmed.wav')

        scores = [self.sentencemodel[cname.replace(',',' ')].score(record_mfcc) for cname in self.sentences]
        predict_word = np.argmax(scores)
        print(scores)
        print(self.sentences[predict_word].replace(',',' '),scores[predict_word])
        # print(self.sentences[predict_word].replace(',',' '))
        return self.sentences[predict_word].replace(',',' ')

    def create_sentence(self):
        with open('Dataset/Sentence.txt', 'w' , encoding='utf-8') as file:
            for action in self.words:
                if action.category != 3:
                    continue

                if action.name == 'Hủy':
                    for food in self.words:
                        if food.category == 2 and not ['Tương Ớt','Nước Nắm','Nước Tương'].__contains__(food.name):
                            file.write(action.name + ',' + food.name + '\n')
                elif action.name == 'Thêm':
                    for quantity in self.words:
                        if quantity.category == 1:
                            for food in self.words:
                                if food.category == 2 and not ['Tương Ớt','Nước Nắm','Nước Tương'].__contains__(food.name):
                                    file.write(action.name + ',' + quantity.name + ',' + food.name + '\n')
                elif action.name == 'Cần':
                    for food in self.words:
                        if food.category == 2 and ['Tương Ớt','Nước Nắm','Nước Tương'].__contains__(food.name):
                            file.write(action.name + ',' + food.name + '\n')
                    file.write(action.name + ',Nhân Viên\n')
                elif action.name == 'Xem':
                    file.write(action.name + ',Menu\n')
        file.close()
    def train(self):
        for idx, i in enumerate(self.words):
            start_prob, trans_matrix = self._init_by_bakis(i.state, 2)
            self.model[i.__str__()] = hmm.GMMHMM(
                n_components=i.state,
                verbose=True,
                n_iter=200,
                startprob_prior=start_prob,
                transmat_prior=trans_matrix,
                params='stmc',
                init_params='mc',
                random_state=42,
                tol=0.001,
                algorithm='viterbi',
                covariance_type='diag'
            )
            self.model[i.__str__()].n_mix = 4
            print()
            print("------------------ Train : ", i.__str__(), "--------------------")
            self.model[i.__str__()].fit(X=np.vstack(self.X['train'][i.__str__()]),
                                   lengths=[x.shape[0] for x in self.X['train'][i.__str__()]])
            print("------------------- Done -----------------------")

    def combine_model(self):

        for sentence in self.sentences:
            if sentence == '':
                continue
            all_word = sentence.split(',')
            all_model = {}
            for word in all_word:
                all_model[word] = self.model[word]

            state = 0
            means = []
            covars = []
            weights = []
            # startprob = []
            for word in all_word:
                means.append(all_model[word].means_)
                covars.append(all_model[word].covars_)
                weights.append(all_model[word].weights_)
                # startprob.append(all_model[word].startprob_)
                state += all_model[word].n_components

            n_model = hmm.GMMHMM(
                n_components=state,
                algorithm='viterbi',
                covariance_type='diag',
            )


            n_model.n_mix = 4
            n_model.means_ = np.concatenate(tuple(means), axis=0)
            n_model.covars_ = np.concatenate(tuple(covars), axis=0)
            n_model.weights_ = np.concatenate(tuple(weights), axis=0)
            start_prob, trans_matrix = self._init_by_bakis(state, 7)
            n_model.transmat_ = self._get_transmat_prior(state, 2)
            n_model.startprob_ = start_prob

            # import pandas
            #
            # df = pandas.read_csv('Dataset/Sentence_test.txt')
            #
            # paths = df['Path'].loc[df.Label == sentence.replace(',',' ')]
            # print(paths)
            # data = [preprocessing.get_mfcc(file_path) for file_path in paths]
            # n_model.fit(X=np.vstack(data),
            #                        lengths=[x.shape[0] for x in data])

            self.sentencemodel[sentence.replace(',',' ')] = n_model


    def get_accuracy(self,savepath=None):
        """

        :param savepath: Path to save the accuracy
        :return:
        """
        y_true = []
        y_pred = []


        kq = []
        log_likehoods = []
        for i in self.words:
            log_likehood = []
            for mfcc, target in zip(self.X['train'][i.__str__()], self.y['train'][i.__str__()]):
                scores = [self.model[cname.__str__()].score(mfcc) for cname in self.words]
                pred = np.argmax(scores)
                y_pred.append(pred)
                y_true.append(target)
                log_likehood.append(scores[pred])

            # print(i.__str__())
            # print('Mean:',np.mean(log_likehood),' ' ,'Std:', np.std(log_likehood))

            self.thresholds[i.__str__()] = np.mean(log_likehood) - 2 * np.std(log_likehood)

            # print('Threshold: ',np.mean(log_likehood) - 2 * np.std(log_likehood))
            acc_in_train = (np.array(y_true) == np.array(y_pred)).sum() / len(y_true)
            pred_in_train = y_pred
            y_true = []
            y_pred = []

            for mfcc, target in zip(self.X['test'][i.__str__()], self.y['test'][i.__str__()]):
                scores = [self.model[cname.__str__()].score(mfcc) for cname in self.words]
                pred = np.argmax(scores)
                y_pred.append(pred)
                y_true.append(target)

            acc_in_test = (np.array(y_true) == np.array(y_pred)).sum() / len(y_true)
            pred_in_test = y_pred

            kq.append([i.__str__(),
                       # acc_in_train,
                       # [self.words[x].__str__() for x in pred_in_train if x != y_true[0]],
                       acc_in_test,
                       [self.words[x].__str__() for x in pred_in_test if x != y_true[0]]
                       ]
                      )
            y_true = []
            y_pred = []

        from tabulate import tabulate
        print(tabulate(kq, headers=["Name", "Accuracy", "Classification error"]))

        if savepath is not None:
            import time
            t = time.localtime()
            current_time = time.strftime("%d_%m_%Y_%H_%M_%S", t)
            Path(os.path.join(savepath,'accuracy_' + current_time + '.txt')).write_text(tabulate(kq, headers=["Name", "Accuracy", "Classification error"]),encoding='utf8')


    def DrawConvergence(self):
        figure, axis = plt.subplots(9, 2 , figsize=(20,20 ))
        row = 0
        col = 0
        figure.tight_layout(pad=3.0)
        for i in self.words:

            if col == 2:
                col = 0
            history = self.model[i.__str__()].monitor_.history

            loss = []
            for j in range(1,history.__len__()-1):
                loss.append(history[j] - history[j-1])
            print(row,col)
            axis[row, col].set_title(i.__str__())
            axis[row, col].set_xlabel("Iteration")
            axis[row,col].plot(loss)

            axis[row,col].set_ylabel("Delta")
            col += 1

            if col == 2:
                row += 1

        plt.show()
    def save_model(self,path):
        """save model to file"""
        f = open(path, 'wb')
        pickle.dump(self, f, pickle.HIGHEST_PROTOCOL)
        f.close()


logging.getLogger("hmmlearn").setLevel("CRITICAL")





if __name__ == '__main__':

    # Train Model
    gmm = GMM(words)
    gmm.init_model()
    gmm.sentences.remove('')
    gmm.train()
    gmm.DrawConvergence()
    gmm.get_accuracy(savepath='logs')
    gmm.create_sentence()
    gmm.combine_model()
    gmm.save_model(path='models_train/model.pkl')


    # Eluavate Model
    df = pandas.read_csv('Dataset/Sentence_test.txt')
    error = 0
    y_true = []
    y_pred = []
    errordetail = []
    total = 0
    all = [x.replace(',',' ') for x in gmm.sentences]
    time_predict = []

    for path, lable in zip(df['Path'], df['Label']):

        if lable not in all :
            continue

        start = time.process_time()

        predict = gmm.predict_file(path)
        y_pred.append(predict)
        y_true.append(lable)
        if predict != lable:
            error += 1
            errordetail.append([path,predict,lable ])
            print("Error",lable)
        total += 1

        time_predict.append(time.process_time() - start)
    print("Test sentence sample:", total)

    print(tabulate(errordetail, headers=['File Name','Predict','Lable'], tablefmt='fancy_grid'))
    print('Accuracy:', metrics.accuracy_score(y_true,y_pred))

    cf_matrix = confusion_matrix(y_true, y_pred)

    print(cf_matrix)
    _ = sns.heatmap(cf_matrix, fmt=".0f", annot=True)
    _ = plt.title("Confusion Matrix")

    plt.show()
    print(metrics.classification_report(y_true, y_pred))

    print(time_predict)
    indexes = list(range(1, len(time_predict) + 1))
    fig, axs = plt.subplots(1, 1, sharey=True)
    axs.plot(indexes, time_predict, color='purple')
    axs.set_ylabel('Process time')
    fig.show()
    from statistics import mean
    print(mean(time_predict))



