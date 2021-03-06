/* Copyright (2007-2012) Schibsted ASA
 *   This file is part of Possom.
 *
 *   Possom is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Possom is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Possom.  If not, see <http://www.gnu.org/licenses/>.
 */
package no.sesat.search.site.config;


import no.sesat.commons.ioc.BaseContext;
import no.sesat.commons.ioc.ContextWrapper;
import no.sesat.search.site.SiteContext;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/** Factory class for deserialising configurations from an xml document.
 *
 *
 * @version $Id$
 */
public abstract class AbstractConfigFactory<C> {

    public interface Context extends BaseContext, SiteContext, BytecodeContext{};

    // Constants -----------------------------------------------------

    private static final Logger LOG = Logger.getLogger(AbstractConfigFactory.class);
    private static final String INFO_CONSTRUCT = "  Construct ";

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    protected AbstractConfigFactory() {
        super();
    }

    // Public --------------------------------------------------------

    public boolean supported(final String xmlName, final Context context) {

        try {
            return null != findClass(xmlName, context);
        }catch (ClassNotFoundException e) {
            LOG.debug("Class not found: " + xmlName);
            return false;
        }
    }

    // Z implementation ----------------------------------------------

    // Y overrides ---------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    protected C construct(final Element element, final Context context) {

        final String xmlName = element.getTagName();
        LOG.debug(INFO_CONSTRUCT + xmlName);
        try {
            return findClass(xmlName, context).newInstance();
        }catch (InstantiationException ex) {
            throw new RuntimeException(ex); // TODO find better exception to throw
        }catch (IllegalAccessException ex) {
            throw new RuntimeException(ex); // TODO find better exception to throw
        }catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    protected abstract Class<C> findClass(final String xmlName, final Context context) throws ClassNotFoundException;

    @SuppressWarnings(value = "unchecked")
    protected Class<C> loadClass(
            final Context context,
            final String classNameFQ,
            final Spi spi) throws ClassNotFoundException {

            final SiteClassLoaderFactory.Context c = ContextWrapper.wrap(
                    SiteClassLoaderFactory.Context.class,
                    new BaseContext() {
                        public Spi getSpi() {
                            return spi;
                        }
                    },
                    context
                );

        final ClassLoader classLoader = SiteClassLoaderFactory.instanceOf(c).getClassLoader();
        return (Class<C>) classLoader.loadClass(classNameFQ);
    }

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
