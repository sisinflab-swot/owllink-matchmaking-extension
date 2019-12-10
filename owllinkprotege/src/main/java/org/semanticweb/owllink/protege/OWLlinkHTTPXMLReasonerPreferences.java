/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, derivo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owllink.protege;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;

/**
 * Author: Olaf Noppens
 * Date: 14.05.2010
 */
public class OWLlinkHTTPXMLReasonerPreferences {
    private static String KEY = "org.semanticweb.owllink";
    private static OWLlinkHTTPXMLReasonerPreferences INSTANCE;

    public static synchronized OWLlinkHTTPXMLReasonerPreferences getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OWLlinkHTTPXMLReasonerPreferences();
        }
        return INSTANCE;
    }


    private Preferences getPreferences() {
        return PreferencesManager.getInstance().getApplicationPreferences(KEY);
    }

    public boolean isUseCompression() {
        return getPreferences().getBoolean("useCompresseion", true);
    }

    public void setUseCompression(boolean useCompression) {
        getPreferences().getBoolean("useCompression", useCompression);
    }

    public void setServerEndpointURL(String url) {
        getPreferences().getString("serverURL", url);
    }

    public String getServerEndpointURL() {
        return getPreferences().getString("serverURL", "http://localhost");
    }

    public int getServerEndpointPort() {
        return getPreferences().getInt("serverPort", 8080);
    }

    public void setServerEndpointPort(int port) {
        getPreferences().putInt("serverPort", port);
    }

}
