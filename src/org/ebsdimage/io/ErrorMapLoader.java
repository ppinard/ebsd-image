package org.ebsdimage.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.ErrorMap;
import org.ebsdimage.core.IndexedByteMap;

import rmlimage.core.Map;

/**
 * Loader for <code>ErrorMap</code>.
 * 
 * @author ppinard
 */
public class ErrorMapLoader extends IndexedByteMapLoader<ErrorCode> {

    @Override
    protected String getFileHeader() {
        return ErrorMap.FILE_HEADER;
    }



    @Override
    public ErrorMap load(File file) throws IOException {
        return (ErrorMap) super.load(file);
    }



    @Override
    protected Class<? extends ErrorCode> getItemClass() {
        return ErrorCode.class;
    }



    @Override
    public ErrorMap load(File file, Map map) throws IOException {
        return (ErrorMap) super.load(file, map);
    }



    @Override
    public ErrorMap load(File file, Object map) throws IOException {
        return (ErrorMap) super.load(file, map);
    }



    @Override
    protected IndexedByteMap<ErrorCode> createMap(int width, int height,
            java.util.Map<Integer, ErrorCode> items) {
        return new ErrorMap(width, height, items);
    }

}
