from firebase import firebase
import time
import json

firebase_url = 'https://restaurant-app-21f27-default-rtdb.firebaseio.com'

class Table:
    def __init__( self,
                  _capacity,
                  _floor,
                  _idTable,
                  _is_start_record,
                  _nameTable,
                  _statusTB,
                  _status_rq,
                  _time_rq,
                  _sttN4,
                  _lor_C,
                  _is_tranfer_foods,
                  _time_rq_tranfer
                ):
        self.capacity = _capacity
        self.floor = _floor
        self.idTable = _idTable
        self.is_start_record = _is_start_record
        self.is_tranfer_foods = _is_tranfer_foods
        self.nameTable = _nameTable
        self.statusTB = _statusTB
        self.status_rq = _status_rq
        self.time_rq_tranfer = _time_rq_tranfer
        self.time_rq = _time_rq
        self.sttN4 = _sttN4
        self.lor_C = _lor_C


def decoder(obj):
    return Table( obj['capacity'] , 
                  obj['floor'] , 
                  obj['idTable'],
                  obj['is_start_record'],
                  obj['nameTable'],
                  obj['statusTB'],
                  obj['status_rq'],
                  obj['time_rq'],
                  obj['sttN4'],
                  obj['lor_C'],
                  obj['is_tranfer_foods'],
                  obj['time_rq_tranfer']
                 )

class Working_with_Firebase:
    def __init__(self):
          self.firebase = firebase.FirebaseApplication(firebase_url, None)
          self.table_list = [ json.loads(json.dumps(i), object_hook=decoder)  for i in self.firebase.get('/Table', None).values() ]

    def get_list_table(self):
        self.table_list = [ json.loads(json.dumps(i), object_hook=decoder)  for i in self.firebase.get('/Table', None).values() ]
        # [print(i.time_rq) for i in self.table_list]

    def get_table_request(self):
        self.get_list_table()
        print('Get table from firebase')
        temp =  list(filter(lambda x: x.status_rq == '1' ,self.table_list))

        from datetime import datetime
        if len(temp) > 0:
            min = datetime.strptime(temp[0].time_rq,'%H:%M:%S')
            target = temp[0]
            for i in  temp:
                t = datetime.strptime(i.time_rq,'%H:%M:%S')
                if t < min:
                    print(t)
                    min = t
                    targer = i
            return target
        return None
    def get_is_start_record(self ,tablerequest):
        self.get_list_table()
        print('Get table from firebase')

        for i in self.table_list:
            if i.idTable == tablerequest.idTable:
                return i.is_start_record
            
    def get_is_tranfer_foods(self):
        self.get_list_table()
        print('Get table from firebase')
        temp =  list(filter(lambda x: x.is_tranfer_foods == 1 ,self.table_list))

        from datetime import datetime
        if len(temp) > 0:
            min = datetime.strptime(temp[0].time_rq_tranfer,'%H:%M:%S')
            target = temp[0]
            for i in  temp:
                t = datetime.strptime(i.time_rq_tranfer,'%H:%M:%S')
                if t < min:
                    print(t)
                    min = t
                    targer = i
            return target
        return None
    

    def get_table_request_tranfer(self,idTable):
        self.get_list_table()
        for i in self.table_list:
            if i.idTable == idTable:
                return i
    def update_is_tranfer_foods(self, table,status):
        self.firebase.put('/Table/' + table.idTable ,data=status,name='is_tranfer_foods')
    def update_is_start_record(self, table):
        self.firebase.put('/Table/' + table.idTable ,data='false',name='is_start_record')
    def update_status(self, table):
        self.firebase.put('/Table/' + table.idTable ,data='0',name='status_rq')
