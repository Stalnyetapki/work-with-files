package guru.qa.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import guru.qa.models.CurrenciesSettings;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import static org.assertj.core.api.Assertions.assertThat;

public class ParsingFileTests {

    ClassLoader cl = ParsingFileTests.class.getClassLoader();

    @Test
    void UnpackingAndParseZipFilesTest() throws Exception {

        try (
                InputStream resource = cl.getResourceAsStream("files/test.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")) {
                    PDF content = new PDF(zis);
                    assertThat(content.author).contains("Мартин Грубер");
                } else if (entry.getName().endsWith(".xlsx")) {
                    XLS content = new XLS(zis);
                    assertThat(content.excel.getSheetAt(0).getRow(3).getCell(0).getStringCellValue()).contains("\"advcash\"");
                } else if (entry.getName().endsWith(".csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(1)[1]).contains("798.00000");
                }
            }
        }
    }

    @Test
    void currenciesSettingsJsonParseTest() throws Exception {

        try (InputStream resource = cl.getResourceAsStream("files/response.json")) {
            ObjectMapper mapper = new ObjectMapper();
            CurrenciesSettings currenciesSettings = mapper.readValue(resource, CurrenciesSettings.class);
            assertThat(currenciesSettings.isGameFiat).isFalse();
            assertThat(currenciesSettings.isFixedRate).isTrue();
            assertThat(currenciesSettings.isViewFiat).isTrue();
            assertThat(currenciesSettings.currencies.get(0).currencyCode).isEqualTo("ALL");
            assertThat(currenciesSettings.currencies.get(0).id).isEqualTo("e69bb7cf-d0b1-4be5-9319-edd6447091cb");
            assertThat(currenciesSettings.currencies.get(1).currencyCode).isEqualTo("AMD");
            assertThat(currenciesSettings.currencies.get(1).id).isEqualTo("b6a5f982-2900-4bea-9acc-9976831b2bd9");
        }


    }
}
