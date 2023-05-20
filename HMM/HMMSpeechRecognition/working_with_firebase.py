from IPython.core.interactiveshell import get_py_filename
import pytz
from firebase import firebase
from datetime import datetime

firebase_url = 'https://restaurant-app-21f27-default-rtdb.firebaseio.com'


class Working_with_Firebase:
    # Hàm chọn bàn => Tạo mới 1 order => Tạo mới các DetailOrder tại bàn đó

    # Hàm này khởi tạo một đối tượng của lớp Order với tham số đầu vào là một URL của Firebase Realtime Database.
    def __init__(self, firebase_url):
        self.firebase = firebase.FirebaseApplication(firebase_url, None)
        self.product_list = list(self.firebase.get('/Product', None).values())
        self.table_list = list(self.firebase.get('/Table', None).values())
        self.order_list = list(self.firebase.get('/Order', None).values())
        self.detailOrder_list = list(self.firebase.get('/DetailOrder', None).values())
        self.request_list = list(self.firebase.get('/Request', None).values())
        self.tz = pytz.timezone('Asia/Ho_Chi_Minh')
        self.menu = []
        for p in self.product_list:
            self.menu.append(p['nameProduct'])

    # Nếu muốn in ra list thì chỉ cần:
    # print(selt.+...)
    # VD: print(self.product_list)

    ####################################################################################################################################################
    # PRODUCT
    # Hàm xem danh sách món ăn (menu Product)
    def List_Product(self):
        print("Danh sách món ăn của quán")
        # print(self.product_list)
        for i, name in enumerate(self.menu):
            print(f"{i + 1}: {name}")

    # Hàm getId_NewProduct: Tạo mới một id product
    def getId_NewProduct(self):
        idProduct = ""
        i = 1
        while True:
            if i < 10:
                idProduct = "Pd0" + str(i)
            else:
                idProduct = "Pd" + str(i)

            # Kiểm tra xem idProduct đã tồn tại chưa
            check = True
            for product in self.product_list:
                if product['idProduct'] == idProduct:
                    # print(f"Món ăn với mã '{idProduct}' đã tồn tại.")
                    check = False
                    break

            if check:
                return idProduct

            i += 1

    # Hàm add_new_product:
    # namePd: Tên món ăn (String)
    # pricesPd: Giá món ăn (Float)
    # detailPd: Mô tả món ăn (String)
    # urlPd: Ảnh món ăn (String)

    def add_new_product(self, namePd, pricesPd, detailPd, urlPd):
        idPd = self.getId_NewProduct()
        new_product = {
            'idProduct': idPd,
            'nameProduct': namePd,
            'pricesProduct': pricesPd,
            'detailProduct': detailPd,
            'rateProduct': 0,
            'urlProduct': urlPd
        }

        # Thêm sản phẩm mới vào Firebase
        self.firebase.put('/Product', idPd, new_product)

        # Cập nhật danh sách sản phẩm trên máy tính cá nhân
        self.product_list = list(self.firebase.get('/Product', None).values())
        print(f"Sản phẩm với mã '{new_product['idProduct']}' đã được thêm thành công.")

    # Hàm delete_product:
    # id_product: id sản phẩm cần xóa (String)
    # name_product: Tên sản phẩm cần xóa (String)
    def delete_Product(self, id_product, name_product):
        # Kiểm tra xem sản phẩm có tồn tại trong Firebase không
        check = False
        for product in self.product_list:
            if product['idProduct'] == id_product and product['nameProduct'] == name_product:
                check = True
                break

        if check:
            # Xóa sản phẩm khỏi Firebase
            for key, value in self.firebase.get('/Product', None).items():
                if value['idProduct'] == id_product and value['nameProduct'] == name_product:
                    self.firebase.delete('/Product', key)
                    break

            # Cập nhật lại danh sách sản phẩm trên máy tính cá nhân
            self.product_list = list(self.firebase.get('/Product', None).values())
            print(f"Món ăn '{name_product}' (id: {id_product}) đã được xóa thành công.")
        else:
            print(f"Món ăn '{name_product}' (id: {id_product}) không tồn tại.")

    # Hàm chỉnh sửa thông tin món ăn
    # name: tên sản phẩm muốn chỉnh sửa (String)
    # id: id sản phẩm muốn chỉnh sửa (String)
    def edit_product(self, name, id):
        # Kiểm tra xem sản phẩm có tồn tại trong danh sách hay không
        product = None
        for p in self.product_list:
            if p['idProduct'] == id and p['nameProduct'] == name:
                product = p
                break

        if product is None:
            print(f"Sản phẩm với tên '{name}' và id '{id}' không tồn tại.")
        else:
            # Chỉnh sửa thông tin sản phẩm
            print(f"Thông tin sản phẩm '{name}' (id: '{id}'):")
            print(f"1. Tên sản phẩm: {product['nameProduct']}")
            print(f"2. Giá sản phẩm: {product['pricesProduct']}")
            print(f"3. Mô tả sản phẩm: {product['detailProduct']}")
            print(f"4. Đánh giá sản phẩm: {product['rateProduct']}")
            print(f"5. Url ảnh sản phẩm: {product['urlProduct']}")

            choice = input("Nhập số thứ tự thông tin muốn chỉnh sửa (nhập '0' để kết thúc): ")

            while choice != "0":
                if choice == "1":
                    new_name = input("Nhập tên mới cho sản phẩm: ")
                    product['nameProduct'] = new_name
                elif choice == "2":
                    new_price = float(input("Nhập giá mới cho sản phẩm: "))
                    product['pricesProduct'] = new_price
                elif choice == "3":
                    new_detail = input("Nhập mô tả mới cho sản phẩm: ")
                    product['detailProduct'] = new_detail
                elif choice == "4":
                    new_rate = float(input("Nhập đánh giá mới cho sản phẩm: "))
                    product['rateProduct'] = new_rate
                elif choice == "5":
                    new_url = input("Nhập url mới cho ảnh sản phẩm: ")
                    product['urlProduct'] = new_url
                else:
                    print("Lựa chọn không hợp lệ.")
                choice = input("Nhập số thứ tự thông tin muốn chỉnh sửa (nhập '0' để kết thúc): ")

            # Cập nhật thông tin sản phẩm trong Firebase
            self.firebase.put('/Product', product['idProduct'], product)

            # Cập nhật danh sách sản phẩm trên máy tính cá nhân
            self.product_list = list(self.firebase.get('/Product', None).values())
            print(f"Sản phẩm với tên '{name}' (id: '{id}') đã được chỉnh sửa thành công.")

    # Hàm get_product_id_by_name: Hàm lấy id món ăn theo tên món ăn
    def get_product_id_by_name(self, nameProduct):
        for product in self.product_list:
            if product['nameProduct'] == nameProduct:
                return product['idProduct']
        print(f"Món ăn với tên '{product}' không tồn tại")
        return None

    def get_NamePd_by_id(self, idProduct):
        for product in self.product_list:
            if product['idProduct'] == idProduct:
                return product['nameProduct']
        return None

    # Hàm get_pricesProduct_by_name: Hàm lấy giá món ăn theo id món ăn
    def get_pricesProduct_by_name(self, idProduct):
        check = False
        for product in self.product_list:
            if product['idProduct'] == idProduct:
                check = True
                a = float(product['pricesProduct'])
        if check:
            return a
        else:
            return float(0)
            print(f"Món ăn với tên '{product}' không tồn tại")

    ####################################################################################################################################################
    # TABLE
    # Hàm xem danh sách Table
    def List_Table(self):
        print(self.table_list)

    # Hàm getId_NewTable: Tạo mới một id table
    def getId_NewTable(self):
        idTable = ""
        i = 1
        while True:
            if i < 10:
                idTable = "Tb0" + str(i)
            else:
                idTable = "Tb" + str(i)

            # Kiểm tra xem idTable đã tồn tại chưa
            check = True
            for table in self.table_list:
                if table['idTable'] == idTable:
                    # print(f"Bàn ăn với mã '{idTable}' đã tồn tại.")
                    check = False
                    break

            if check:
                return idTable

            i += 1

    # Hàm add_new_table:
    # nameTb : tên bàn ăn (String)
    # capacityTb : số chỗ ngồi của bàn ăn (int)
    # floorTb : Vị trí tầng lầu chứa bàn ăn (int)
    def add_new_table(self, nameTb, capacityTb, floorTb):
        idTb = self.getId_NewTable()
        statusTb = ""
        status_rq_Tb = ""
        time_rq = ""
        is_start_record = False
        lor_C = 0
        sttN4 = 0
        new_table = {
            'idTable': idTb,
            'nameTable': nameTb,
            'capacity': capacityTb,
            'floor': floorTb,
            'statusTB': statusTb,
            'status_rq': status_rq_Tb,
            'time_rq': time_rq,
            'is_start_record': is_start_record,
            'lor_C': lor_C,
            'sttN4': sttN4

        }

        # Thêm bàn ăn mới vào Firebase
        self.firebase.put('/Table', idTb, new_table)

        # Cập nhật danh sách bàn ăn trên máy tính cá nhân
        self.table_list = list(self.firebase.get('/Table', None).values())
        print(f"Bàn ăn với mã '{new_table['idTable']}' đã được thêm thành công.")

    # Hàm delete_table
    # id_table: Id bàn ăn cần xóa (String)
    # name_table : Tên bàn ăn cần xóa (String)
    def delete_table(self, id_table, name_table):
        # Kiểm tra xem bàn ăn có tồn tại trong danh sách Firebase không
        check = False
        for table in self.table_list:
            if table['idTable'] == id_table and table['nameTable'] == name_table:
                check = True
                break

        if check:
            # Xóa bàn ăn khỏi Firebase
            for key, value in self.firebase.get('/Table', None).items():
                if value['idTable'] == id_table and value['nameTable'] == name_table:
                    self.firebase.delete('/Table', key)
                    break

            # Cập nhật lại danh sách bàn ăn trên máy tính cá nhân
            self.table_list = list(self.firebase.get('/Table', None).values())
            print(f"Bàn ăn '{name_table}' (id: {id_table}) đã được xóa thành công.")
        else:
            print(f"Bàn ăn '{name_table}' (id: {id_table}) không tồn tại.")

    # Hàm chỉnh sửa thông tin bàn ăn
    # name : Tên bàn ăn muốn chỉnh sửa (String)
    # id : id bàn ăn muốn chỉnh sửa (String)
    def edit_table(self, name, id):
        # Kiểm tra xem bàn ăn có tồn tại trong danh sách hay không
        table = None
        for p in self.table_list:
            if p['idTable'] == id and p['nameTable'] == name:
                table = p
                break

        if table is None:
            print(f"Bàn ăn với tên '{name}' và id '{id}' không tồn tại.")
        else:
            # Chỉnh sửa thông tin bàn ăn
            print(f"Thông tin bàn ăn '{name}' (id: '{id}'):")
            print(f"1. Tên bàn ăn: {table['nameTable']}")
            print(f"2. Số chỗ ngồi của bàn ăn: {table['capacity']}")
            print(f"3. Vị trí tầng lầu chứa bàn ăn: {table['floor']}")

            choice = input("Nhập số thứ tự thông tin muốn chỉnh sửa (nhập '0' để kết thúc): ")

            while choice != "0":
                if choice == "1":
                    new_name = input("Nhập tên mới cho bàn ăn: ")
                    table['nameTable'] = new_name
                elif choice == "2":
                    new_capacity = float(input("Nhập số chỗ ngồi mới cho bàn ăn: "))
                    table['capacity'] = new_capacity
                elif choice == "3":
                    new_floor = input("Nhập vị trí tầng lầu mới chứa bàn ăn: ")
                    table['floor'] = new_floor
                else:
                    print("Lựa chọn không hợp lệ.")
                choice = input("Nhập số thứ tự thông tin muốn chỉnh sửa (nhập '0' để kết thúc): ")

            # Cập nhật thông tin bàn ăn trong Firebase
            self.firebase.put('/Table', table['idTable'], table)

            # Cập nhật danh sách bàn ăn trên máy tính cá nhân
            self.table_list = list(self.firebase.get('/Table', None).values())
            print(f"Bàn ăn với tên '{name}' (id: '{id}') đã được chỉnh sửa thành công.")

    # Hàm get_table_id_by_name: Hàm lấy id table theo tên table
    def get_table_id_by_name(self, nameTable):
        for table in self.table_list:
            if table['nameTable'] == nameTable:
                return table['idTable']
        print(f"Bàn ăn với tên '{nameTable}' không tồn tại")
        return None

    ####################################################################################################################################################
    # ORDER

    # Hàm xem danh sách order
    def List_Order(self):
        print(self.order_list)

    # Hàm getId_NewOrder: Tạo mới một id Order
    def getId_NewOrder(self):
        idO = ""
        i = 1
        while True:
            if i < 10:
                idO = "Ord0" + str(i)
            else:
                idO = "Ord" + str(i)

            # Kiểm tra xem idO đã tồn tại chưa
            check = True
            for order in self.order_list:
                if order['idOrder'] == idO:
                    # print(f"Order với mã '{idO}' đã tồn tại.")
                    check = False
                    break

            if check:
                return idO

            i += 1

    # Hàm add_new_order theo tên bàn
    def add_new_order(self, nameTable):
        now = datetime.now(self.tz)
        current_time = str(now.strftime("%Y-%m-%d %H:%M:%S"))
        idO = self.getId_NewOrder()
        statusOrdered = "Serving..."
        idAcc = "Acc08"
        totalBill = 0
        # Kiểm tra xem có Order nào tại bàn ăn đó đang serving không
        check = False
        for order in self.order_list:
            if order['idTable'] == self.get_table_id_by_name(nameTable) and order['statusOrdered'] == "Serving...":
                check = True
                break
        if check:
            print(f"Hiện tại, bàn ăn với tên '{nameTable}' đang có order")
        else:
            new_order = {
                'dtimeOrder': current_time,
                'idAcc': idAcc,
                'idOrder': idO,
                'idTable': self.get_table_id_by_name(nameTable),
                'statusOrdered': statusOrdered,
                'totalBill': totalBill,
            }

            # Thêm order mới vào Firebase
            self.firebase.put('/Order', idO, new_order)

            # Cập nhật danh sách order trên máy tính cá nhân
            self.order_list = list(self.firebase.get('/Order', None).values())
            print(f"Order với mã '{new_order['idOrder']}' đã được thêm thành công.")

    # Hàm delete_order: Xoá order (đồng thời xoá tất cả DetailOrder có idOrder) của bàn ăn đang được phục vụ
    def delete_order(self, nameTable):
        # Kiểm tra xem sản phẩm có tồn tại trong Firebase không
        check = False

        # Xóa order khỏi Firebase
        for key, value in self.firebase.get('/Order', None).items():
            if value['idOrder'] == self.get_table_order_serving(nameTable):
                self.delete_All_DetailOrder_By_idOrder(nameTable)
                self.firebase.delete('/Order', key)
                check = True
                break

            # Cập nhật lại danh sách Order trên máy tính cá nhân
            self.order_list = list(self.firebase.get('/Order', None).values())
        if check:
            print(f"Order của bàn '{nameTable}' đã được xóa thành công.")
        else:
            print(f"Order của bàn '{nameTable}' không tồn tại.")

    # Hàm set_totalbill_order(): Hàm set tổng giá trị hoá đơn của bàn ăn đang được phục vụ
    def set_totalbill_order(self, nameTable):
        order = None
        sum = 0
        # Kiểm tra xem bàn ăn đang phục vụ có tồn tại order nào trong danh sách hay không
        for p in self.order_list:
            if p['idOrder'] == self.get_table_order_serving(nameTable):
                order = p
                break
        # print(order['idOrder'])
        if order is None:
            print(f"Bàn ăn '{nameTable}' không tồn tại order nào.")

        else:
            for dtOrder in self.detailOrder_list:
                if dtOrder['idOrder'] == order['idOrder']:
                    sum = sum + (dtOrder['quantity'] * self.get_pricesProduct_by_name(dtOrder['idProduct']))
            order['totalBill'] = sum
            # Cập nhật thông tin order trong Firebase
            self.firebase.put('/Order', order['idOrder'], order)

            # Cập nhật danh sách order trên máy tính cá nhân
            self.order_list = list(self.firebase.get('/Order', None).values())

    # Hàm get_totalbill_order(): Hàm get tổng giá trị hoá đơn của bàn ăn đang được phục vụ
    def get_totalbill_order(self, nameTable):
        order = None
        sum = 0
        # Kiểm tra xem bàn ăn đang phục vụ có tồn tại order nào trong danh sách hay không
        for p in self.order_list:
            if p['idOrder'] == self.get_table_order_serving(nameTable):
                order = p
                break
        if order is None:
            print(f"Bàn ăn '{nameTable}' không tồn tại order nào.")

        else:
            # print(order['idOrder'])
            self.set_totalbill_order(nameTable)
            return order['totalBill']

    # Hàm edit_order: Hàm chỉnh sửa trạng thái order của bàn ăn đang được phục vụ thành 'complete' sau khi đã thanh toán xong
    # choice : Trạng thái thanh toán (String)
    # "1": Thanh toán rồi
    # "2": Chưa thanh toán
    def edit_order_complete(self, nameTable, choice):
        order = None
        # Kiểm tra xem bàn ăn đang phục vụ có tồn tại order nào trong danh sách hay không
        for p in self.order_list:
            if p['idOrder'] == self.get_table_order_serving(nameTable):
                order = p
                break

        if order is None:
            print(f"Bàn ăn '{nameTable}' không tồn tại order nào.")
        else:
            # Chỉnh sửa thông tin order bàn ăn
            print(f"Thông tin order của bàn ăn '{nameTable}':")
            print(f"Thời gian order: {order['dtimeOrder']}")
            print(f"Tổng giá trị hoá đơn: {order['totalBill']}")
            print(f"Trạng thái phục vụ cuả Order: {order['statusOrdered']}")
            if choice == "1":
                order['statusOrdered'] = "Complete"
                print(f"Thông tin order của bàn ăn sau khi cập nhật '{nameTable}':")
                print(f"Thời gian order: {order['dtimeOrder']}")
                print(f"Tổng giá trị hoá đơn: {order['totalBill']}")
                print(f"Trạng thái phục vụ cuả Order: {order['statusOrdered']}")
            elif choice == "2":
                print("Order của bàn ăn đang được phục vụ chưa thanh toán, không thể chỉnh sửa trạng thái của order")
            else:
                print("Trạng thái thanh toán không hợp lệ.")
            # Cập nhật thông tin order trong Firebase
            self.firebase.put('/Order', order['idOrder'], order)

            # Cập nhật danh sách order trên máy tính cá nhân
            self.order_list = list(self.firebase.get('/Order', None).values())
            print(f"Order của bàn ăn'{nameTable}' đã được chỉnh sửa thành công.")

    # Hàm get_order_id_by_nameTable: Hàm lấy id order theo tên table
    def get_order_id_by_nameTable(self, nameTable):
        for order in self.order_list:
            if order['idTable'] == self.get_table_id_by_name(nameTable):
                if order['statusOrdered'] == "Serving...":
                    return order['idOrder']
        print(f"Order của bàn ăn có tên '{nameTable}' không tồn tại")
        return None

    # Hàm get order serving theo tên table
    def get_table_order_serving(self, nameTable):
        for order in self.order_list:
            if order['idTable'] == self.get_table_id_by_name(nameTable) and order['statusOrdered'] == "Serving...":
                return order['idOrder']
                break
        return None

    ####################################################################################################################################################
    # DETAIL ORDER
    # Hàm xem danh sách detail_order
    def List_DetailOrder(self):
        print(self.detailOrder_list)

    # Hàm xem ds Oder của bàn:
    def List_OrderTable(self, name_table):
        id_Order = self.get_order_id_by_nameTable(name_table)
        if id_Order is None:
            return "Bạn chưa gọi món nào !"
        else:
            print(id_Order)
            text = "Bạn đã gọi"

            for detail_Order in self.detailOrder_list:
                if detail_Order['idOrder'] == id_Order:
                    text += " " + str(detail_Order['quantity']) + " " + str(
                        self.get_NamePd_by_id(detail_Order['idProduct'])) + " ; "
            print(text)
            if text == "Bạn đã gọi":
                return "Bạn chưa gọi món nào !"
            else:
                return text

    # Hàm getId_NewDetailOrder: Tạo mới một id Detail Order
    def getId_NewDetailOrder(self):
        idDO = ""
        i = 1
        while True:
            if i < 10:
                idDO = "DO0" + str(i)
            else:
                idDO = "DO" + str(i)

            # Kiểm tra xem idDO đã tồn tại chưa
            check = True
            for detailorder in self.detailOrder_list:
                if detailorder['idDetailOrder'] == idDO:
                    # print(f"Detail order với mã '{idDO}' đã tồn tại.")
                    check = False
                    break

            if check:
                return idDO

            i += 1

    # Hàm add_new_DetailOrder
    # nameTable : Tên bàn ăn muốn Order (String)
    # nameProduct : Món ăn muốn Order: (String)
    # quantity : Số lượng của món ăn muốn Order (int)
    def add_new_DetailOrder(self, nameTable, nameProduct, quantity):
        idDO = self.getId_NewDetailOrder()
        idOrder = ""
        statusDetailOrder = "Not Done"
        # Kiểm tra xem có Order nào tại bàn ăn đó đang serving không
        check = False
        for order in self.order_list:
            if order['idOrder'] == self.get_table_order_serving(nameTable):
                idOrder = order['idOrder']
                check = True
                break
        if check:
            new_DetailOrder = {
                'idDetailOrder': idDO,
                'idOrder': idOrder,
                'idProduct': self.get_product_id_by_name(nameProduct),
                'quantity': quantity,
                'statusDetailOrder': statusDetailOrder,
            }
            # Thêm Detailorder mới vào Firebase
        #  self.firebase.put('/DetailOrder', idDO, new_DetailOrder)

        # # Cập nhật danh sách Detail order trên máy tính cá nhân
        #  self.detailOrder_list = list(self.firebase.get('/DetailOrder', None).values())
        #  print(f"DetailOrder với mã '{new_DetailOrder['idDetailOrder']}' đã được thêm thành công.")
        #  self.set_totalbill_order(nameTable)
        #  text = "Đã thêm " + str(quantity) + " " +  str(nameProduct)
        else:
            self.add_new_order(nameTable)
            for order in self.order_list:
                if order['idOrder'] == self.get_table_order_serving(nameTable):
                    idOrder = order['idOrder']
                    break
            new_DetailOrder = {
                'idDetailOrder': idDO,
                'idOrder': idOrder,
                'idProduct': self.get_product_id_by_name(nameProduct),
                'quantity': quantity,
                'statusDetailOrder': statusDetailOrder,
            }
        # Thêm Detail order mới vào Firebase
        self.firebase.put('/DetailOrder', idDO, new_DetailOrder)

        # Cập nhật danh sách Detail order trên máy tính cá nhân
        self.detailOrder_list = list(self.firebase.get('/DetailOrder', None).values())
        text = "Đã thêm " + str(quantity) + " " + str(nameProduct)
        self.set_totalbill_order(nameTable)
        return text

    # Hàm delete_DetailOrder: có sẵn tham số (tên bàn, tên món ăn)
    def delete_DetailOrder(self, nameTable, nameProduct):
        # Kiểm tra xem có DetailOrder(có idorder trùng với order nào với tên bàn và tên món ăn đó hay không,
        # có đang serving hay ko, và trạng thái có đang là "Not Done" hay không) tồn tại trong Firebase không
        check = False
        # idDt=""
        for detailorder in self.detailOrder_list:
            idDt = ""
            if detailorder['idOrder'] == self.get_table_order_serving(nameTable) and detailorder[
                'idProduct'] == self.get_product_id_by_name(nameProduct) and detailorder[
                'statusDetailOrder'] == "Not Done":
                idDt = detailorder['idDetailOrder']
                check = True
                # break
            if check:
                # Xóa DetailOrder đó khỏi Firebase
                for key, value in self.firebase.get('/DetailOrder', None).items():
                    if value['idDetailOrder'] == idDt:
                        self.firebase.delete('/DetailOrder', key)
                        break
        if check:
            # Cập nhật lại danh sách Detail trên máy tính cá nhân
            self.detailOrder_list = list(self.firebase.get('/DetailOrder', None).values())
            # print(f"DetailOrder có món '{nameProduct}' của bàn '{nameTable}' đang được phục vụ đã xóa thành công.")
            self.set_totalbill_order(nameTable)
            return "Đã hủy món " + str(nameProduct)
        else:
            # print(f"DetailOrder có món '{nameProduct}' của bàn '{nameTable}' đang được phục vụ không tồn tại.")
            return "Món này chưa được gọi"

            # Hàm delete_All_DetailOrder_By_idOrder(): Hàm xoá tất cả DetailOrder theo tên bàn ăn đang được phục vụ

    def delete_All_DetailOrder_By_idOrder(self, nameTable):
        check = False
        for key, value in self.firebase.get('/DetailOrder', None).items():
            if value['idOrder'] == self.get_table_order_serving(nameTable):
                self.firebase.delete('/DetailOrder', key)
                check = True
                # Cập nhật lại danh sách DetailOrder trên máy tính cá nhân
        self.detailOrder_list = list(self.firebase.get('/DetailOrder', None).values())
        if check:
            print(f"Tất cả DetailOrder của bàn '{nameTable}' đang được phục vụ đã xóa thành công.")
        else:
            print(f"Không tồn tại DetailOrder nào của bàn '{nameTable}' đang được phục vụ.")

        self.set_totalbill_order(nameTable)

    ####################################################################################################################################################
    # REQUEST
    # Hàm xem danh sách tất cả request
    def List_request(self):
        print(self.request_list)

    # Hàm xem danh sách tất cả request theo tên bàn, khi thực thi phải truyền vào tham số NameTable( Tên bàn ăn)
    def List_request_table(self, NameTable):
        self.request_list_of_table = []
        for r in self.request_list:
            if r['NameTable'] == NameTable:
                self.request_list_of_table.append(r)
        print(self.request_list_of_table)

    # Hàm getId_NewRequest: Tạo mới một id request
    def getId_NewRequest(self):
        idRequest = ""
        i = 1
        while True:
            if i < 10:
                idRequest = "Rq0" + str(i)
            else:
                idRequest = "Rq" + str(i)

            # Kiểm tra xem idRequest đã tồn tại chưa
            check = True
            for r in self.request_list:
                if r['idRequest'] == idRequest:
                    # print(f"Yêu cầu với mã '{idRequest}' đã tồn tại.")
                    check = False
                    break

            if check:
                return idRequest

            i += 1

    # Hàm add_new_Request, khi thực thi phải truyền vào hai tham số là NameTable (tên bàn ăn) và Content(Nội dung yêu cầu):
    def add_new_Request(self, NameTable, Content):
        idRq = self.getId_NewRequest()
        now = datetime.now(self.tz)
        current_time = str(now.strftime("%Y-%m-%d %H:%M:%S"))
        resolve = False

        new_Request = {
            'idRequest': idRq,
            'NameTable': NameTable,
            'Content': Content,
            'Time': current_time,
            'resolve': resolve
        }

        # Thêm request mới vào Firebase
        self.firebase.put('/Request', idRq, new_Request)

        # Cập nhật danh sách request trên máy tính cá nhân
        self.request_list = list(self.firebase.get('/Request', None).values())
        # print(f"Request với mã '{new_Request['idRequest']}' đã được thêm thành công.")
        text = str(Content) + " đã được ghi nhận"
        print(text)
        return text

    #############################################################################################

    def textToRequest(self, nameTable, sentence):
        acction = ["hủy", "thêm", "xem", "cần"]
        quality_to_integer = {"một": 1, "hai": 2, "ba": 3, "bốn": 4, "năm": 5, "sáu": 6, "bảy": 7, "tám": 8, "chín": 9,
                              "mười": 10}
        food = ["bún bò", "mỳ quảng", "thịt nướng", "mực hấp", ]
        other = ["tương ớt", "nước tương", "nhân viên"]
        if "hủy" in sentence:
            for element_f in food:
                if element_f in sentence:
                    return self.delete_DetailOrder(nameTable, element_f)
        if "thêm" in sentence:
            for number_str in quality_to_integer:
                if number_str in sentence:
                    number_int = quality_to_integer[number_str]
                    for element_f in food:
                        if element_f in sentence:
                            return self.add_new_DetailOrder(nameTable, element_f, number_int)
        if "cần" in sentence:
            for element_s in other:
                if element_s in sentence:
                    return self.add_new_Request(nameTable, "yêu cầu " + str(element_s))

        if "xem order" == sentence:
            return self.List_OrderTable(nameTable)
        return "yêu cầu này chúng tôi không phục vụ"


####################################################################################################################################################
# TEST CLASS

# Working1 = Working_with_Firebase(firebase_url)

# Working1.edit_order_complete("Table 2","1")

# Working1.add_new_DetailOrder("Table 2","gà quay",3)
# Working1.textToRequest("Table 2", "cần nhân viên")
# Working1.delete_DetailOrder("Table 3", "bún bò" )

# Working1.textToRequest("Table 3", "hủy bún bò")

# Working1.List_DetailOrder()
# Working1.add_new_Request("Table 4","Tôi cần nhân viên")


# Working1.List_request_table("Table 1")




