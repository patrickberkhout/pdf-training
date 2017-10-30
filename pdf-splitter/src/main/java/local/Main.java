package local;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class Main {
	public static void main(String[] args) {
		try {
			FileWriter writer = new FileWriter("studiegidsen.csv");
			writer.write("DOCUMENT,PAGE,CONTENT\n");
			for (String file : args) {
				new Main().split(new File(file), writer);
			}
			writer.close();
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void split(File myPDF, FileWriter writer) throws InvalidPasswordException, IOException {
		PDDocument document = PDDocument.load(myPDF);
		Splitter splitter = new Splitter();
		List<PDDocument> splittedDocuments = splitter.split(document);
		int i = 1;
		// for (PDDocument doc : splittedDocuments) {
		// doc.save(myPDF.getPath() + "." + i++ + ".pdf");
		// }
		

		for (PDDocument doc : splittedDocuments) {
			writer.write(myPDF.getName() +  "," + i++ + "," + contentFrom(doc) + "\n");
			doc.close();
		}
	}

	private String contentFrom(PDDocument doc) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			doc.save(os);
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
