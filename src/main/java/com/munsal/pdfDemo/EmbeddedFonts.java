/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.munsal.pdfDemo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

/**
 * An example of using an embedded TrueType font with Unicode text.
 *
 * @author Keiji Suzuki
 * @author John Hewson
 */
public final class EmbeddedFonts
{
    
    public void createPDFFromImage(String inputFile, String imagePath, String outputFile )
        throws IOException
    {
        // the document
        PDDocument doc = null;
        try
        {
            doc = PDDocument.load( new File(inputFile) );
            
            //we will add the image to the first page.
            PDPage page = doc.getPage(0);
            
            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage,
            // call LosslessFactory.createFromImage() instead
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            
            // contentStream.drawImage(ximage, 20, 20 );
            // better method inspired by http://stackoverflow.com/a/22318681/535646
            // reduce this value if the image is too large
            float scale = 1f;
            contentStream.drawImage(pdImage, 20, 20, pdImage.getWidth()*scale, pdImage.getHeight()*scale);
            
            contentStream.close();
            doc.save( outputFile );
        }
        finally
        {
            if( doc != null )
            {
                doc.close();
            }
        }
    }

    private EmbeddedFonts()
    {
    }
    
    public static void main(String[] args) throws IOException
    {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
//        PDFont font = PDType1Font.HELVETICA_BOLD;
        String dir = "../pdfDemo/src/main/resources/ttf/liberation_sans/";
        String imagePath = "../pdfDemo/src/main/resources/images/imageTrial.jpg";
    
        PDType0Font font = PDType0Font.load(document, new File(dir + "LiberationSans-Regular.ttf"));

        PDPageContentStream stream = new PDPageContentStream(document, page);
    
    
//        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
    
        // contentStream.drawImage(ximage, 20, 20 );
        // better method inspired by http://stackoverflow.com/a/22318681/535646
        // reduce this value if the image is too large
        float scale = 1f;
        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
    
    
    
    
    

        stream.beginText();
        stream.setFont(font, 12);
        stream.setLeading(12 * 1.2f);

        stream.newLineAtOffset(50, 300);
        stream.showText("PDFBox's Unicode with Embedded TrueType Font");
        stream.newLine();

        stream.showText("Supports full Unicode text ☺");
        stream.newLine();

        stream.showText("English trial");
        stream.newLine();

        // ligature
        stream.showText("Ligatures:hi ");
//        stream.newLine();
//        stream.newLine();
//        stream.newLine();
//        stream.newLine();
//        stream.newLine();
        stream.endText();
        stream.drawImage(pdImage, 20, 20, 200, 200);
    
    
        stream.close();

        document.save("example.pdf");
        document.close();
    }
}
