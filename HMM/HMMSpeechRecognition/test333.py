from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import LinearSVC
from sklearn.feature_selection import SelectFromModel
from sklearn.model_selection import cross_val_score
from sklearn.pipeline import make_pipeline
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.feature_selection import VarianceThreshold
from sklearn.datasets import make_classification

# Lấy dữ liệu example từ package sklearn
X, y = make_classification()

logit = LogisticRegression()
# Hồi qui theo RandomForest
rdFrt = RandomForestClassifier(n_estimators = 10, random_state = 1)
# Hồi qui theo LinearSVC
lnSVC = LinearSVC(C=0.01, penalty="l1", dual=False)
# Tạo một pipeline thực hiện lựa chọn biến từ RandomForest model và hồi qui theo logit
pipe1 = make_pipeline(StandardScaler(), SelectFromModel(estimator = rdFrt), logit)
# Tạo một pipeline thực hiện lựa chọn biến từ Linear SVC model và hồi qui theo logit
pipe2 = make_pipeline(StandardScaler(), SelectFromModel(estimator = lnSVC), logit)
# Cross validate đối với
# 1. Mô hình logit
acc_log = cross_val_score(logit, X, y, scoring = 'accuracy', cv = 5).mean()
# 2. Mô hình RandomForest
acc_rdf = cross_val_score(rdFrt, X, y, scoring = 'accuracy', cv = 5).mean()
# 3. Mô hình pipe1
acc_pip1 = cross_val_score(pipe1, X, y, scoring = 'accuracy', cv = 5).mean()
# 3. Mô hình pipe2
acc_pip2 = cross_val_score(pipe2, X, y, scoring = 'accuracy', cv = 5).mean()

print('Accuracy theo logit:', acc_log)
print('Accuracy theo random forest:', acc_rdf)
print('Accuracy theo pipeline 1:', acc_pip1)
print('Accuracy theo pipeline 2:', acc_pip2)