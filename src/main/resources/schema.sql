-- CSDL hệ thống siêu thị trực tuyến - Đề 10 - B23DCDT076
-- Mỗi lớp thực thể lưu bền có một bảng; lớp con dùng khóa chính đồng thời là khóa ngoại tới bảng lớp cha.
-- Các lớp thống kê (ItemStatistics6...) là dữ liệu dẫn xuất nên không có bảng.
CREATE TABLE IF NOT EXISTS tblUser6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  fullName VARCHAR(120) NOT NULL,
  dateOfBirth DATE,
  address VARCHAR(255),
  email VARCHAR(254) UNIQUE,
  phoneNumber VARCHAR(20) UNIQUE,
  role VARCHAR(30) NOT NULL
);
CREATE TABLE IF NOT EXISTS tblCustomer6 (
  userId INT PRIMARY KEY,
  customerCode VARCHAR(20) NOT NULL UNIQUE,
  registeredDate DATE NOT NULL,
  FOREIGN KEY (userId) REFERENCES tblUser6(id)
);
CREATE TABLE IF NOT EXISTS tblEmployee6 (
  userId INT PRIMARY KEY,
  employeeCode VARCHAR(20) NOT NULL UNIQUE,
  position VARCHAR(80),
  FOREIGN KEY (userId) REFERENCES tblUser6(id)
);
CREATE TABLE IF NOT EXISTS tblSupplier6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  supplierCode VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(150) NOT NULL,
  address VARCHAR(255),
  email VARCHAR(254),
  phoneNumber VARCHAR(20),
  description VARCHAR(500)
);
CREATE TABLE IF NOT EXISTS tblItemCategory6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  description VARCHAR(500)
);
CREATE TABLE IF NOT EXISTS tblItem6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  itemCode VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(150) NOT NULL,
  unit VARCHAR(30) NOT NULL,
  sellingPrice DECIMAL(15,2) NOT NULL CHECK (sellingPrice >= 0),
  quantityOnHand INT NOT NULL DEFAULT 0 CHECK (quantityOnHand >= 0),
  status VARCHAR(20) NOT NULL,
  description VARCHAR(500),
  categoryId INT NOT NULL,
  FOREIGN KEY (categoryId) REFERENCES tblItemCategory6(id)
);
CREATE TABLE IF NOT EXISTS tblTransaction6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  transactionCode VARCHAR(20) NOT NULL UNIQUE,
  transactionDate TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL,
  note VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS tblTransactionDetail6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  quantity INT NOT NULL CHECK (quantity > 0),
  unitPrice DECIMAL(15,2) NOT NULL CHECK (unitPrice >= 0),
  transactionId INT NOT NULL,
  itemId INT NOT NULL,
  FOREIGN KEY (transactionId) REFERENCES tblTransaction6(id),
  FOREIGN KEY (itemId) REFERENCES tblItem6(id)
);
CREATE TABLE IF NOT EXISTS tblGoodsReceipt6 (
  transactionId INT PRIMARY KEY,
  supplierId INT NOT NULL,
  warehouseStaffId INT NOT NULL,
  FOREIGN KEY (transactionId) REFERENCES tblTransaction6(id),
  FOREIGN KEY (supplierId) REFERENCES tblSupplier6(id),
  FOREIGN KEY (warehouseStaffId) REFERENCES tblEmployee6(userId)
);
CREATE TABLE IF NOT EXISTS tblOnlineOrder6 (
  transactionId INT PRIMARY KEY,
  customerId INT NOT NULL,
  approvedById INT,
  shippingAddress VARCHAR(255),
  FOREIGN KEY (transactionId) REFERENCES tblTransaction6(id),
  FOREIGN KEY (customerId) REFERENCES tblCustomer6(userId),
  FOREIGN KEY (approvedById) REFERENCES tblEmployee6(userId)
);
CREATE TABLE IF NOT EXISTS tblCounterSale6 (
  transactionId INT PRIMARY KEY,
  salerId INT NOT NULL,
  customerId INT,
  FOREIGN KEY (transactionId) REFERENCES tblTransaction6(id),
  FOREIGN KEY (salerId) REFERENCES tblEmployee6(userId),
  FOREIGN KEY (customerId) REFERENCES tblCustomer6(userId)
);
CREATE TABLE IF NOT EXISTS tblDelivery6 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  deliveryDate TIMESTAMP,
  status VARCHAR(20) NOT NULL,
  onlineOrderId INT NOT NULL UNIQUE,
  deliveryStaffId INT NOT NULL,
  FOREIGN KEY (onlineOrderId) REFERENCES tblOnlineOrder6(transactionId),
  FOREIGN KEY (deliveryStaffId) REFERENCES tblEmployee6(userId)
);
CREATE INDEX IF NOT EXISTS idxTransactionDate6 ON tblTransaction6(transactionDate, status);
CREATE INDEX IF NOT EXISTS idxDetailItem6 ON tblTransactionDetail6(itemId, transactionId);
