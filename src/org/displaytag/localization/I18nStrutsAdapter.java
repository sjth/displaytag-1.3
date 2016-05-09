/**
 * Licensed under the Artistic License; you may not use this file
 * except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://displaytag.sourceforge.net/license.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.displaytag.localization;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.interceptor.I18nInterceptor;
import org.apache.struts2.util.TextProviderHelper;
import org.apache.struts2.views.jsp.TagUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * TODO: getResource method may be broken
 * Changed this class to make it work with Struts 2.3.28.1 - untested. 
 * getResource method may not work. Use at your own risk.
 * The original author's credits are retained below this comment block.
 * 
 * @verson displaytag 1.3
 * @date 2016-05-06
 * 
 */

/**
 * Struts implementation of a resource provider and locale resolver. Uses Struts
 * <code>RequestUtils.getUserLocale()</code> and <code>TagUtils.message()</code> for the lookup.
 * @author Fabrizio Giustina
 * @version $Revision: 1081 $ ($Author: fgiust $)
 */
public class I18nStrutsAdapter implements I18nResourceProvider, LocaleResolver
{

    /**
     * prefix/suffix for missing entries.
     */
    public static final String UNDEFINED_KEY = "???"; //$NON-NLS-1$

    /**
     * @see LocaleResolver#resolveLocale(HttpServletRequest)
     */
    public Locale resolveLocale(HttpServletRequest request)
    {
        Locale userLocale = null;
        HttpSession session = request.getSession(false);

        if (session != null)
        {
            userLocale = (Locale) session.getAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
        }
        
        if ((userLocale == null) && (ActionContext.getContext()!= null))
        {
        	userLocale = (Locale) ActionContext.getContext().get(ActionContext.LOCALE);
        }

        if (userLocale == null)
        {
            // Returns Locale based on Accept-Language header or the server default
            userLocale = request.getLocale();
        }

        return userLocale;
    }

    /**
     * This method may still be broken. Good luck! :-/
     * @see I18nResourceProvider#getResource(String, String, Tag, PageContext)
     */
    public String getResource(String resourceKey, String defaultValue, Tag tag, PageContext pageContext)
    {
        String key = (resourceKey != null) ? resourceKey : defaultValue;
        ValueStack stack = TagUtils.getStack(pageContext);
        String value = TextProviderHelper.getText(resourceKey, defaultValue, stack);
        if (value == null && resourceKey != null)
        {
            value = UNDEFINED_KEY + resourceKey + UNDEFINED_KEY;
        }

        return value;
    }

}
