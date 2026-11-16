// Chụp ảnh giao diện web đang chạy tại http://127.0.0.1:8090 (dùng trong GitHub Actions).
const puppeteer = require('puppeteer-core');
const fs = require('fs');
const BASE = 'http://127.0.0.1:8090';
const OUT = 'screenshots';
const iso = d => d.toISOString().slice(0, 10);
const today = new Date();
const minus = n => { const d = new Date(today); d.setDate(d.getDate() - n); return iso(d); };

(async () => {
  fs.mkdirSync(OUT, { recursive: true });
  const browser = await puppeteer.launch({ executablePath: process.env.CHROME_PATH || '/usr/bin/google-chrome',
    args: ['--no-sandbox', '--lang=vi-VN'], defaultViewport: { width: 1280, height: 800 } });
  const page = await browser.newPage();
  const shot = async name => { await page.screenshot({ path: `${OUT}/${name}.png`, fullPage: true }); console.log('saved', name); };
  const go = async url => { await page.goto(BASE + url, { waitUntil: 'networkidle0' }); };
  const setVal = async (sel, v) => { await page.$eval(sel, (el, v) => { el.value = v; }, v); };
  const submit = async sel => { await Promise.all([page.waitForNavigation({ waitUntil: 'networkidle0' }), page.click(sel)]); };

  await go('/'); await shot('L01_trang_chu');
  await go('/register'); await shot('L02_form_dang_ky');
  // Lỗi: thiếu họ tên, email sai, mật khẩu không khớp, username đã tồn tại
  await setVal('#dateOfBirth', '2005-01-15'); await setVal('#address', 'Hà Đông, Hà Nội');
  await setVal('#phoneNumber', '0987654321'); await setVal('#email', 'duy@abc');
  await setVal('#username', 'manager6'); await setVal('#password', 'MatKhau@123'); await setVal('#confirmPassword', 'MatKhau@12');
  await submit('button[type=submit].primary'); await shot('L03_dang_ky_loi_du_lieu');
  // Trùng username/email/sđt (dữ liệu đúng định dạng)
  await go('/register');
  await setVal('#fullName', 'Nguyễn Văn An'); await setVal('#dateOfBirth', '2001-05-10'); await setVal('#address', 'Cầu Giấy, Hà Nội');
  await setVal('#phoneNumber', '0912345678'); await setVal('#email', 'an.nguyen@example.com');
  await setVal('#username', 'khachhang6'); await setVal('#password', 'MatKhau@123'); await setVal('#confirmPassword', 'MatKhau@123');
  await submit('button[type=submit].primary'); await shot('L04_dang_ky_trung_thong_tin');
  // Thành công
  await go('/register');
  await setVal('#fullName', 'Nguyễn Bá Duy'); await setVal('#dateOfBirth', '2005-01-15'); await setVal('#address', 'Hà Đông, Hà Nội');
  await setVal('#phoneNumber', '0987654321'); await setVal('#email', 'duy.nb@example.com');
  await setVal('#username', 'duynb6'); await setVal('#password', 'MatKhau@123'); await setVal('#confirmPassword', 'MatKhau@123');
  await submit('button[type=submit].primary'); await shot('L05_dang_ky_thanh_cong');

  await go('/login'); await shot('L06_dang_nhap');
  await setVal('#username', 'manager6'); await setVal('#password', 'Manager@123');
  await submit('button[type=submit].primary'); await shot('L07_trang_chu_quan_ly');
  await go('/reports'); await shot('L08_menu_thong_ke');
  await go('/reports/items'); await shot('L09_chon_khoang_ngay');
  await go(`/reports/items?startDate=${minus(0)}&endDate=${minus(5)}`); await shot('L10_loi_khoang_ngay');
  await go(`/reports/items?startDate=${minus(60)}&endDate=${minus(40)}`); await shot('L11_khong_co_du_lieu');
  await go(`/reports/items?startDate=${minus(30)}&endDate=${minus(0)}`); await shot('L12_danh_sach_thong_ke');
  const links = await page.$$eval('a.row-link', as => as.map(a => a.getAttribute('href')));
  await page.goto(BASE.replace(/\/$/, '') + links[0], { waitUntil: 'networkidle0' }); await shot('L13_chi_tiet_mat_hang');
  await page.goto(BASE + links[2], { waitUntil: 'networkidle0' }); await shot('L14_chi_tiet_mat_hang_ngung_ban');
  await browser.close();
})().catch(e => { console.error(e); process.exit(1); });
