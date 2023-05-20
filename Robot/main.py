from SerialArduino import SerialArduino
from TableFirebase import Table,Working_with_Firebase
from Text2speech import Text2Speech
from goto import with_goto
import time

@with_goto
def main():
    serialarduino = SerialArduino()
    workingwithfirebase = Working_with_Firebase()
    while True:
        
        tablerequest = workingwithfirebase.get_table_request()
        tablerequesttranfer = workingwithfirebase.get_is_tranfer_foods()


        if not tablerequest is None:

            label .begin
            Text2Speech("đi đến bàn " + tablerequest.nameTable.replace('Table ',''))
            serialarduino.Write(text=('go to table:' + str(tablerequest.sttN4)+ ',' + str(tablerequest.lor_C) ) )

            serialarduino.Read('gone target table:')

            Text2Speech(u'Đã đến bàn ' + tablerequest.nameTable.replace('Table ',''))

            # Xu Li Tieng 
            Text2Speech(u'Bắt đầu gọi món , trước khi gọi món vui long bấm nút đỏ')

            count = 0
            flag = 0
            while True:

                if flag == 0 and count == 30:
                    flag = 1
                    Text2Speech('Nếu 10 giây nữa bạn không nói gì robot sẽ về kết thúc phục vụ')
                    count = 0
                elif flag == 1 and count == 10:
                    Text2Speech('Kết thúc phục vụ. Hẹn gặp lại')
                    break
                status = workingwithfirebase.get_is_start_record(tablerequest)
                print(status)
                if status == 'true':
                    from PredictSpeech import predict_wavfile
                    predict_wavfile()
                    workingwithfirebase.update_is_start_record(tablerequest)
                    count = 0
                else:
                    count += 1
                time.sleep(1)
            time.sleep(3)

            workingwithfirebase.update_status(tablerequest)

            label .go_to

            tablerequest = workingwithfirebase.get_table_request()

            serialarduino.Write('Continue')
            if tablerequest is None:
                Text2Speech(u'Về vị trí ban đầu')
                serialarduino.Write('go to root')

                serialarduino.Read('goto target root')
                Text2Speech(u'Đã Về vị trí ban đầu')
            else:
                 goto .begin

        elif not tablerequesttranfer is None:
            
            Text2Speech(u'Vui lòng bỏ đồ ăn lên robot')
            status = workingwithfirebase.get_table_request_tranfer(tablerequesttranfer.idTable).is_tranfer_foods
            while status == 1:
                status = workingwithfirebase.get_table_request_tranfer(tablerequesttranfer.idTable).is_tranfer_foods
                time.sleep(1)

            Text2Speech(u'Bắt đầu vận chuyển đồ ăn')
            Text2Speech("đi đến bàn " + tablerequesttranfer.nameTable.replace('Table ',''))
            serialarduino.Write(text=('go to table:' + str(tablerequesttranfer.sttN4)+ ',' + str(tablerequesttranfer.lor_C) ) )

            serialarduino.Read('gone target table:')

            Text2Speech(u'Đã đến bàn ' + tablerequesttranfer.nameTable.replace('Table ',''))

            # Xu Li Tieng 
            Text2Speech(u'Bạn có mười phút để mang món ăn từ robot vào bàn. Xin cảm ơn')
            time.sleep(10)
            workingwithfirebase.update_is_tranfer_foods(tablerequesttranfer,0)

            goto .go_to

        time.sleep(1)


main()