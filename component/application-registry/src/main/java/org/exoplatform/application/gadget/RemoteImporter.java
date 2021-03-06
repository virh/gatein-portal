/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.application.gadget;

import java.io.IOException;
import java.net.URL;

import org.apache.shindig.gadgets.spec.ModulePrefs;
import org.exoplatform.application.gadget.impl.GadgetDefinition;
import org.exoplatform.application.gadget.impl.RemoteGadgetData;
import org.gatein.common.net.URLTools;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class RemoteImporter extends GadgetImporter {

    public RemoteImporter(String name, String gadgetPath) {
        super(name, gadgetPath);
    }

    @Override
    protected byte[] getGadgetBytes(String gadgetURI) throws IOException {
        URL url = new URL(gadgetURI);
        return URLTools.getContent(url, 5000, 5000);
    }

    @Override
    protected String getGadgetURL() {
        return getGadgetURI();
    }

    @Override
    protected void process(String gadgetURI, GadgetDefinition def) throws Exception {
        def.setLocal(false);

        //
        RemoteGadgetData data = (RemoteGadgetData) def.getData();

        // Set remote URL
        data.setURL(gadgetURI);
    }

    @Override
    protected void processMetadata(ModulePrefs prefs, GadgetDefinition def) {
        String gadgetName = def.getName();
        String description = prefs.getDescription();
        String thumbnail = prefs.getThumbnail().toString();
        String title = getGadgetTitle(prefs, gadgetName);
        String referenceURL = prefs.getTitleUrl().toString();

        log.info("Importing gadget name=" + gadgetName + " description=" + description + " thumbnail=" + thumbnail + " title="
                + thumbnail + " title=" + title);

        def.setDescription(description);
        def.setThumbnail(thumbnail);
        def.setTitle(title);
        def.setReferenceURL(referenceURL);

    }

    private String getGadgetTitle(ModulePrefs prefs, String defaultValue) {
        String title = prefs.getDirectoryTitle();
        if (title == null || title.trim().length() < 1) {
            title = prefs.getTitle();
        }
        if (title == null || title.trim().length() < 1) {
            return defaultValue;
        }
        return title;
    }
}
