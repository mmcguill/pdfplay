import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 * Created by mark on 10/05/2017.
 */
public class PdfReadApplication {
    public static void main(String[] args) {
        try {
            PDDocument document = PDDocument.load(new File("samples/Turgot - Reflections on Formation and Distribution of Wealth.pdf"));

            dumpImages(document);

            //dumpText(document);

            System.out.println(document.getDocumentInformation());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void dumpText(PDDocument document) throws IOException {
        if (!document.isEncrypted()) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            PDFTextStripper Tstripper = new PDFTextStripper();
            String st = Tstripper.getText(document);

            System.out.println("Text:" + st);
        }
        else {
            System.out.println("Encrypted");
        }
    }

    private static void dumpImages(PDDocument document) throws IOException {
        PDPageTree list = document.getPages();

        Files.createDirectories(Paths.get("images"));

        int i=0;
        int image=1;
        for (PDPage page : list) {
            System.out.println(String.format("%d/%d", i++, list.getCount()));

            PDResources pdResources = page.getResources();
            for (COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);

                if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
                    File file = new File(String.format("images/%04d", image++) + ".png");
                    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getImage(), "png", file);
                }
                else {
                    System.out.println(o.toString());
                }
            }
        }
    }
}
