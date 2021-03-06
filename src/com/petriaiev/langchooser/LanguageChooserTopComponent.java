/*
 *  LanguageChooserTopComponent.java
 *  Copyright (C) 2013 Viacheslav Petriaiev
 *  petriayev.viacheslav@gmail.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.petriaiev.langchooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.NotificationDisplayer;
import org.openide.modules.Places;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Top component which displays IDE language choosing panel.
 *
 * @author Viacheslav Petriaiev
 */
@ConvertAsProperties(
    dtd = "-//com.petriaiev.langchooser//LanguageChooser//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "LanguageChooserTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "com.petriaiev.langchooser.LanguageChooserTopComponent")
@ActionReference(path = "Menu/Tools" , position = 10000, separatorBefore=9999)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_LanguageChooserAction",
preferredID = "LanguageChooserTopComponent")
public final class LanguageChooserTopComponent extends TopComponent {

    private static final String CONF_FILE_NAME = "netbeans.conf";
    private static final String ETC_FOLDER_PATH = "/etc";
    private static final String NB_LOCALE_KEY = "--locale";
    private static final String NB_LOCALE_SEPARATOR = ":";
    private static final String DEFAULT_NETBEANS_OPTIONS_KEY = "netbeans_default_options";

    private static final Locale[] LOCALES = new Locale[]{
        new Locale("en"),
        new Locale("ja"),
        new Locale("zh", "CN"),
        new Locale("pt", "BR"),
        new Locale("ru"),
    };

    public LanguageChooserTopComponent() {
        setName(NbBundle.getMessage(LanguageChooserTopComponent.class, "CTL_LanguageChooserTopComponent"));
        setToolTipText(NbBundle.getMessage(LanguageChooserTopComponent.class, "HINT_LanguageChooserTopComponent"));
        initComponents();
        initLocalesCB();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        languageCB = new javax.swing.JComboBox();
        saveButton = new javax.swing.JButton();
        warningLabel = new javax.swing.JLabel();
        needShutDown = new javax.swing.JCheckBox();

        languageCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.openide.awt.Mnemonics.setLocalizedText(saveButton, org.openide.util.NbBundle.getMessage(LanguageChooserTopComponent.class, "LanguageChooserTopComponent.saveButton.text")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(warningLabel, org.openide.util.NbBundle.getMessage(LanguageChooserTopComponent.class, "LanguageChooserTopComponent.warningLabel.text")); // NOI18N

        needShutDown.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(needShutDown, org.openide.util.NbBundle.getMessage(LanguageChooserTopComponent.class, "LanguageChooserTopComponent.needShutDown.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(languageCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(needShutDown)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(warningLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(languageCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton)
                    .addComponent(warningLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(needShutDown)
                .addContainerGap(236, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        final File userDir = Places.getUserDirectory();
        final File userConfigFileDir = new File(userDir, ETC_FOLDER_PATH);
        final File userConfigFile = new File(userConfigFileDir, CONF_FILE_NAME);
        if(!userConfigFile.exists()) {
            final File netbeansHome = new File(System.getProperty("netbeans.home"));
            final File commonConfigFileDir = new File(netbeansHome.getParentFile(), ETC_FOLDER_PATH);
            final File commonConfigFile = new File(commonConfigFileDir, CONF_FILE_NAME);
            try {
                copyFile(commonConfigFile, userConfigFile);
                String title = NbBundle.getMessage(LanguageChooserTopComponent.class, "OpenIDE-Module-Name");
                String message = NbBundle.getMessage(LanguageChooserTopComponent.class, "LC_userConfFileCreated");
                NotificationDisplayer.getDefault().notify(title, new ImageIcon(), message, null);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                String message = NbBundle.getMessage(LanguageChooserTopComponent.class, "LC_userConfFileCreateError");
                NotifyDescriptor nd = new NotifyDescriptor.Message(message, NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
            }
        }
        final Locale locale = LOCALES[languageCB.getSelectedIndex()];
        setLocaleToUserConfigFile(userConfigFile, locale);
        if(needShutDown.isSelected()) {
            LifecycleManager.getDefault().exit();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox languageCB;
    private javax.swing.JCheckBox needShutDown;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables

    private void initLocalesCB() {
        languageCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
            LOCALES[0].getDisplayName(LOCALES[0]),
            LOCALES[1].getDisplayName(LOCALES[1]),
            LOCALES[2].getDisplayName(LOCALES[2]),
            LOCALES[3].getDisplayName(LOCALES[3]),
            LOCALES[4].getDisplayName(LOCALES[4]),
        }));
        int localeIdx = 0;
        final Locale currentLocale = Locale.getDefault();
        for(int i = 0; i < LOCALES.length; i++) {
            if(LOCALES[i].getLanguage().equalsIgnoreCase(currentLocale.getLanguage())) {
                if(currentLocale.getCountry() != null && !currentLocale.getCountry().isEmpty()) {
                    if(currentLocale.getCountry().equalsIgnoreCase(LOCALES[i].getCountry())) {
                        localeIdx = i;
                        break;
                    }
                } else {
                    localeIdx = i;
                    break;
                }
            }
        }
        languageCB.setSelectedIndex(localeIdx);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }

    private void setLocaleToUserConfigFile(final File userConfigFile, final Locale locale) {
        try {
            final FileInputStream in = new FileInputStream(userConfigFile);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            final List<String> linesToWrite = new ArrayList<String>();
            while((line = br.readLine()) !=  null) {
                if(line.startsWith(DEFAULT_NETBEANS_OPTIONS_KEY)) {
                    final StringBuilder localeToWriteSb = new StringBuilder(locale.getLanguage());
                    if(locale.getCountry() != null && !locale.getCountry().isEmpty()) {
                        localeToWriteSb.append(NB_LOCALE_SEPARATOR).append(locale.getCountry());
                    }
                    String localeToWrite = localeToWriteSb.toString();
                    if(line.contains(NB_LOCALE_KEY)) {
                        line = line.replaceFirst("(" + NB_LOCALE_KEY + "\\s+)(?:[^\\s'\"]+)($|[\\s\"']+)",
                                "$1" + localeToWrite + "$2");
                    } else {
                        final StringBuilder sb = new StringBuilder(line);
                        line = sb.insert(DEFAULT_NETBEANS_OPTIONS_KEY.length() + 2, NB_LOCALE_KEY + " " + localeToWrite + " ").toString();
                    }
                }
                linesToWrite.add(line);
            }
            br.close();
            in.close();
            final FileWriter fw = new FileWriter(userConfigFile);
            final PrintWriter pw = new PrintWriter(fw);
            for(String lineToWrite : linesToWrite) {
                pw.println(lineToWrite);
            }
            pw.close();
            fw.close();
        } catch(Exception e) {
            Exceptions.printStackTrace(e);
            String message = NbBundle.getMessage(LanguageChooserTopComponent.class, "LC_langApplyError");
            NotifyDescriptor nd = new NotifyDescriptor.Message(message, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
    }

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }
}
