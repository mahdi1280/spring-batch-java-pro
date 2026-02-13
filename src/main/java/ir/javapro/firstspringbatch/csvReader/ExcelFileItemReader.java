package ir.javapro.firstspringbatch.csvReader;

import ir.javapro.firstspringbatch.model.PersonEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ExcelFileItemReader implements ItemReader<PersonEntity> {

    private Iterator<Row> rowIterator;


    public ExcelFileItemReader(Resource resource) throws IOException {
        InputStream is = resource.getInputStream();
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        rowIterator = sheet.iterator();

        if(rowIterator.hasNext()) {
            rowIterator.next();
        }
    }


    @Override
    public @Nullable PersonEntity read() throws Exception {
        if(!rowIterator.hasNext()) {
            return null;
        }

        Row row = rowIterator.next();

        return new PersonEntity(row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getStringCellValue(),
                (int) row.getCell(4).getNumericCellValue()
        );
    }
}
