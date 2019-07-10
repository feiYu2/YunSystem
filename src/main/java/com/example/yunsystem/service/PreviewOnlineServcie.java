package com.example.yunsystem.service;

import com.example.yunsystem.util.JsonResult;
import com.itextpdf.text.DocumentException;


import java.net.URISyntaxException;

public interface PreviewOnlineServcie {
    String  file2Pdf ( String input) throws URISyntaxException, DocumentException;
    public JsonResult pdfDelete( String deletePath);
}
