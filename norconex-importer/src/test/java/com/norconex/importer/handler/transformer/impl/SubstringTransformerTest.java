/* Copyright 2017 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.importer.handler.transformer.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.norconex.commons.lang.config.XMLConfigurationUtil;
import com.norconex.importer.ImporterException;
import com.norconex.importer.doc.ImporterMetadata;
import com.norconex.importer.handler.ImporterHandlerException;

public class SubstringTransformerTest {

    @Test
    public void testTransformTextDocument() 
            throws IOException, ImporterHandlerException {
        String content = "1234567890";
        
        Assert.assertEquals("", substring(0, 0, content));
        Assert.assertEquals(content, substring(-1, -1, content));
        Assert.assertEquals("123", substring(0, 3, content));
        Assert.assertEquals("456", substring(3, 6, content));
        Assert.assertEquals("890", substring(7, 42, content));
        Assert.assertEquals("1234", substring(-1, 4, content));
        Assert.assertEquals("7890", substring(6, -1, content));
        try {
            substring(4, 1, content);
            Assert.fail("Should have triggered an exception.");
        } catch (ImporterException e) {
        }
        
    }
    
    private String substring(long begin, long end, String content) 
            throws ImporterHandlerException {
        InputStream input = new ByteArrayInputStream(content.getBytes());        
        SubstringTransformer t = new SubstringTransformer();
        t.setBegin(begin);
        t.setEnd(end);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        t.transformDocument(
                "N/A", input, output, new ImporterMetadata(), false);
        return new String(output.toByteArray());
    }
    
    @Test
    public void testWriteRead() throws IOException {
        SubstringTransformer t = new SubstringTransformer();
        t.setBegin(1000);
        t.setEnd(5000);
        System.out.println("Writing/Reading this: " + t);
        XMLConfigurationUtil.assertWriteRead(t);
    }

}
