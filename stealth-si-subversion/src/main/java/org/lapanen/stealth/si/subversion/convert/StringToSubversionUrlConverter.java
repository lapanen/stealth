package org.lapanen.stealth.si.subversion.convert;

import java.beans.PropertyEditor;

import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.convert.converter.Converter;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

public class StringToSubversionUrlConverter implements Converter<String, SVNURL> {

    @Override
    public SVNURL convert(final String source) {
        try {
            return SVNURL.parseURIEncoded(source);
        } catch (SVNException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
