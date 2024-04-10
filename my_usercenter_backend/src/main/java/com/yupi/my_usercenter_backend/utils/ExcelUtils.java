package com.yupi.my_usercenter_backend.utils;

import com.yupi.my_usercenter_backend.model.Userinfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 从excel表批量导入用户(未脱敏)
 */
public class ExcelUtils {

    public static List<Userinfo> parseExcel(InputStream inputStream) throws IOException {
        List<Userinfo> users = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Userinfo user = new Userinfo();

            user.setUserName(row.getCell(0).getStringCellValue());
            user.setUserAccount(row.getCell(1).getStringCellValue());
            user.setUserAvatarUrl(row.getCell(2).getStringCellValue());
            user.setGender((int) row.getCell(3).getNumericCellValue());
            user.setPhone(row.getCell(5).getStringCellValue());
            user.setEmail(row.getCell(6).getStringCellValue());
            user.setUserStatus(row.getCell(7).getStringCellValue());
            user.setUserRole((int) row.getCell(11).getNumericCellValue());
            users.add(user);
        }
        workbook.close();
        return users;
    }
}

