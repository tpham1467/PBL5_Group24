import time
import serial

class SerialArduino:

    def __init__(self) -> None:
        self.serial_port = serial.Serial(
            port="/dev/ttyACM0",
            baudrate=115200,
            bytesize=serial.EIGHTBITS,
            parity=serial.PARITY_NONE,
            stopbits=serial.STOPBITS_ONE,
            timeout=5,
            xonxoff=False,
            rtscts=False,
            dsrdtr=False,
            writeTimeout=5
        )
        # Wait a second to let the port initialize


    def Write(self,text):
        while True:
            try:
                self.serial_port.write(text.encode())

                data = self.serial_port.readline().decode()

                if data:
                    data = data.replace('\n','')
                    if 'Received' in data :
                        break
                    else:
                        pass
                time.sleep(1)
            except Exception as e:
                print(e)
                self.serial_port.close()
        print("Done")

    def Read(self,targettext):
        while True:
            try:
                data = self.serial_port.readline().decode()
                if data:
                    data = data.replace('\n','')
                    if targettext in data:
                        break
                    else:
                        pass
                time.sleep(1)
            except Exception as e:
                print(e)
                self.serial_port.close()
        print("Done")
