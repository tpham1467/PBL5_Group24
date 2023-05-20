import matplotlib.pyplot as plt
import pandas as pd
import seaborn
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
import numpy as np;


X_train = pd.DataFrame()
X_test = pd.DataFrame()

def ramdom_NA_replace():
    random_samples = X_train['Age'].dropna().sample(n=X_train['Age'].isnull().sum(), random_state=0)
    random_samples.index = X_train[X_train['Age'].isnull()].index

    random_samples_test = X_train['Age'].dropna().sample(n=X_test['Age'].isnull().sum(), random_state=0)
    random_samples_test.index = X_test[X_test['Age'].isnull()].index

    X_test.loc[X_test['Age'].isnull(), 'Age'] = random_samples_test
    X_train.loc[X_train['Age'].isnull(), 'Age'] = random_samples

def median_NA_repace():
    median = X_train.Age.median()
    X_test.Age.fillna(median, inplace=True)
    X_train.Age.fillna(median, inplace=True)

def mean_NA_repace():
    mean = X_train.Age.mean()
    X_train["Age"].fillna(mean, inplace=True)
    X_test["Age"].fillna(mean, inplace=True)

def mode_imputation_NA_repace():
    mode = X_train.Age.mode()[0]
    X_test["Age"].fillna(mode, inplace=True)
    X_train["Age"].fillna(mode, inplace=True)
def end_of_dict():
    extreme = X_train.Age.mean() + 3 * X_train.Age.std()
    X_test["Age"].fillna(extreme,inplace=True)
    X_train["Age"].fillna(extreme, inplace=True)
def ArbitraryValue():
    X_test["Age"].fillna(100, inplace=True)
    X_train["Age"].fillna(100, inplace=True)
def newfeature():
    median = X_train.Age.median()
    X_train['Age_NAN']=np.where(df['Age'].isnull(),1,0)
    X_train['Age'].fillna(median, inplace=True)

    X_test['Age_NAN'] = np.where(X_test['Age'].isnull(), 1, 0)
    X_test['Age'].fillna(median , inplace=True)

def outlier():

    uppper_boundary = X_train['Age'].mean() + 3 * X_train['Age'].std()
    lower_boundary = X_train['Age'].mean() - 3 * X_train['Age'].std()

    X_train.loc[X_train['Age'] >= uppper_boundary, 'Age'] = uppper_boundary
    X_test.loc[X_test['Age'] >= uppper_boundary, 'Age'] = uppper_boundary


    IQR = X_train.Fare.quantile(0.75) - X_train.Fare.quantile(0.25)
    lower_bridge = X_train['Fare'].quantile(0.25) - (IQR * 3)
    upper_bridge = X_train['Fare'].quantile(0.75) + (IQR * 3)

    X_train.loc[X_train['Fare'] >= upper_bridge, 'Fare'] = upper_bridge
    X_test.loc[X_test['Fare'] >= upper_bridge, 'Fare'] = upper_bridge
def NoneFunc():
    pass



X = lambda: mean_NA_repace()
X0 = lambda: mode_imputation_NA_repace()
X1 = lambda: ramdom_NA_replace()
X2 = lambda: median_NA_repace()
X3 = lambda: end_of_dict()
X4 = lambda: ArbitraryValue()
X5 = lambda: newfeature()
X6 = lambda: outlier()
X7 = lambda: NoneFunc()

name_NA_replace = ["Mean" , "Mode Imputation" , "Ramdom" , "Median" , "End of dict" , "Arbitrary" , "newfeature"]
name_outlier_Replace = ["outlier" , "Not outlier"];

NA_Replace = [X , X0 , X1 , X2 , X3 ,X4 ,X5]
outlier_Replace = [X6,X7]



df1 = pd.read_csv('titanic.csv', usecols=['Age', 'Fare', 'Survived' , 'Sex' , 'SibSp' , 'Parch'])
df1['newSex'] = [1 if i == 'male' else 0 for i in df1.Sex]

NameMin = ''
NameMax = ''
Max = -np.inf
Min = np.inf

Gt = []

for idx , x in enumerate(NA_Replace):
    for idy , y in enumerate(outlier_Replace):

        sum = 0
        for i in range(1,11):
            New_X_train , New_X_test, y_train, y_test = train_test_split(df1[['Age', 'Fare']], df1['Survived'],
                                                                    test_size=0.3 , random_state=i)


            X_train = pd.DataFrame(New_X_train)
            X_test = pd.DataFrame(New_X_test)

            df = pd.DataFrame(X_train)
            x()
            y()

            classifier = LogisticRegression()
            classifier.fit(df, y_train)
            y_pred = classifier.predict(X_test)
            # print("Accuracy_score: {}".format(accuracy_score(y_test, y_pred)))
            sum += accuracy_score(y_test, y_pred);

        if (sum/10) > Max:
            Max = (sum/10)
            NameMax = name_NA_replace[idx] + " " + name_outlier_Replace[idy]
        else:
            Min = (sum/10)
            NameMin = name_NA_replace[idx] + " " + name_outlier_Replace[idy]

        print(name_NA_replace[idx], " ", name_outlier_Replace[idy]);
        print("Accuracy_score: {}".format(sum/10))
        print("------------------------------------------------------------")

print(NameMax ,  ' ' , Max)
print(NameMin, ' ' , Min)


# from sklearn.preprocessing import MinMaxScaler
# from sklearn.preprocessing import minmax_scale
# from sklearn.preprocessing import MaxAbsScaler
# from sklearn.preprocessing import StandardScaler
# from sklearn.preprocessing import RobustScaler
# from sklearn.preprocessing import Normalizer
# from sklearn.preprocessing import QuantileTransformer
# from sklearn.preprocessing import PowerTransformer
#
# import scipy.stats as stat
# import matplotlib.pyplot as plt
#
# def standardscaler():
#     global X_train
#     global X_test
#
#     scaler = StandardScaler().set_output(transform="pandas").fit(X_train)
#
#     X_train = scaler.transform(X_train)
#     X_test = scaler.transform(X_test)
#
# def minmaxscaler():
#     global X_train
#     global X_test
#
#     min_max = MinMaxScaler().set_output(transform="pandas").fit(X_train)
#     X_train = min_max.transform(X_train)
#     X_test = min_max.transform(X_test)
#
#
# def robustscaler():
#     global X_train
#     global X_test
#
#     scaler = RobustScaler().set_output(transform="pandas").fit(X_train)
#
#     X_train = scaler.transform(X_train)
#     X_test = scaler.transform(X_test)
#
# def  maxabsscaler():
#     global X_train
#     global X_test
#
#     scaler = MaxAbsScaler().set_output(transform="pandas").fit(X_train)
#     X_train = scaler.transform(X_train)
#     X_test = scaler.transform(X_test)
#
#
# def transformationlog1p():
#
#     X_train.Fare = np.log1p(X_train['Fare'])
#     X_test.Fare = np.log1p(X_test['Fare'])
#
#
# def transformationboxcox():
#     # plot_data(X_train, "Fare")
#
#     X_train['Fare'], parameters = stat.boxcox(X_train['Fare'] +1)
#     # plot_data(X_train, "Fare")
#     X_test['Fare'], parameters = stat.boxcox(X_test['Fare'] +1)
# def plot_data(df,feature):
#     plt.figure(figsize=(10,6))
#     plt.subplot(1,2,1)
#     df[feature].hist() # histogram
#     plt.subplot(1,2,2)
#     stat.probplot(df[feature],dist='norm',plot=plt)# prob plot
#     plt.show()
#
#
# # print(0.6735074626865672)
# #
# # X = [standardscaler,minmaxscaler,maxabsscaler,robustscaler]
# # Y = [NoneFunc,transformationboxcox,transformationlog1p]
# #
# # for i in X:
# #     for j in Y:
# #         sum = 0
# #         for z in range(10):
# #             New_X_train, New_X_test, y_train, y_test = train_test_split(df1[['Age', 'Fare','newSex' ,'Parch','SibSp']], df1['Survived'],
# #                                                                         test_size=0.3, random_state=z)
# #             X_train = pd.DataFrame(New_X_train)
# #             X_test = pd.DataFrame(New_X_test)
# #
# #             mean_NA_repace()
# #             outlier()
# #
# #             j()
# #             i()
# #
# #             X_train['Parch'] = X_train['Parch'].transform(lambda x: np.power(x, 3))
# #             X_train['SibSp'] = X_train['SibSp'].transform(lambda x: np.power(x, 3))
# #
# #             X_test['Parch'] = X_test['Parch'].transform(lambda x: np.power(x, 3))
# #             X_test['SibSp'] = X_test['SibSp'].transform(lambda x: np.power(x, 3))
# #
# #             classifier = LogisticRegression()
# #             classifier.fit(X_train, y_train)
# #             y_pred = classifier.predict(X_test)
# #             sum = sum + accuracy_score(y_test, y_pred)
# #
# #         Gt.append(sum/10)
# #         # print(i.__str__().replace("<function ","").split(" at ")[0] , "--",j.__str__().replace("<function ","").split(" at ")[0] ,"--", "Accuracy_score: {}".format(sum/10))
# #
# # Gt.sort()
# # print(Gt.pop())
#
#
#
# sum = 0

# for i in range(10):
#     New_X_train, New_X_test, y_train, y_test = train_test_split(df1[['Age', 'Fare','newSex' ,'Parch','SibSp']], df1['Survived'],
#                                                                 test_size=0.3, random_state=i)
#     X_train = pd.DataFrame(New_X_train)
#     X_test = pd.DataFrame(New_X_test)
#
#
#     end_of_dict()
#     outlier()
#
#     MinMaxScaler()
#
#     X_train['Fare'] = X_train['Fare'].transform(lambda x: np.power(x, 3))
#     X_test['Fare'] = X_test['Fare'].transform(lambda x: np.power(x, 3))
#
#     X_train['Parch'] = X_train['Parch'].transform(lambda x: np.power(x, 3))
#     X_train['SibSp'] = X_train['SibSp'].transform(lambda x: np.power(x, 3))
#
#     X_test['Parch'] = X_test['Parch'].transform(lambda x: np.power(x, 3))
#     X_test['SibSp'] = X_test['SibSp'].transform(lambda x: np.power(x, 3))
#
#     # plot_data(X_train,"Parch")
#     # plot_data(X_train, "SibSp")
#
#     from sklearn.feature_selection import SequentialFeatureSelector
#     from sklearn.linear_model import LogisticRegression
#
#
#     logit = LogisticRegression()
#     selector = SequentialFeatureSelector(logit, n_features_to_select=4, )
#
#     selector.fit(X_train, y_train)
#
#     X_train = selector.set_output(transform="pandas").transform(X_train)
#     print(list(X_train))
#     X_test = selector.set_output(transform="pandas").transform(X_test)
#
#     classifier = LogisticRegression()
#     classifier.fit(X_train, y_train)
#     y_pred = classifier.predict(X_test)
#
#     # print(accuracy_score(y_test, y_pred))
#     sum = sum + accuracy_score(y_test, y_pred)
#
# print()
# print(sum/10)