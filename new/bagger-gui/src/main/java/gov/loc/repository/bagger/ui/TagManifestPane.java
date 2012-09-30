package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagit.BagFile;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class TagManifestPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(TagManifestPane.class);
    public static final String TAGMANIFEST_PANE = "tagManifestPane";
    private String messages = new String();
    private BagView parentView;
    private DefaultBag defaultBag;
    private BagTextPane dataPane;
    private JScrollPane dataScrollPane;
    private List<BagTextPane> manifestPaneList;
    private List<JScrollPane> manifestScrollPaneList;
    private Dimension preferredDimension = new Dimension(600, 480);
    private Color selectedColor = Color.lightGray; //new Color(180, 180, 200);
    private Color unselectedColor = Color.black; //new Color(180, 180, 160);

    public TagManifestPane(BagView bagView) {
        super();
        this.parentView = bagView;
        this.defaultBag = bagView.getBag();
        populateBagPane();
    }

    private void init() {
    	setPreferredSize(preferredDimension);
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int count = sourceTabbedPane.getTabCount();
                int selected = sourceTabbedPane.getSelectedIndex();
                for (int i = 0; i < count; ++i) {
                    Color c = (i == selected) ? unselectedColor : selectedColor;
                    sourceTabbedPane.setBackgroundAt(i, c);
                    sourceTabbedPane.setForegroundAt(i, c);
                }
            }
        };
        this.addChangeListener(changeListener);
    }

    public void setBag(DefaultBag bag) {
    	this.defaultBag = bag;
    }

    public DefaultBag getBag() {
    	return this.defaultBag;
    }

    public void setMessages(String messages) {
    	this.messages = messages;
    }

    public String getMessages() {
    	return this.messages;
    }

    public void populateBagPane() {
    	Collection<BagFile> list = defaultBag.getTags();
    	manifestPaneList = new ArrayList<BagTextPane>();
    	manifestScrollPaneList = new ArrayList<JScrollPane>();
    	log.info("TagManifestPane.populateBagPane getTags: " + list.size());
        for (Iterator<BagFile> it=list.iterator(); it.hasNext(); ) {
            BagFile bf = it.next();
            String content = "";
            if (bf.getFilepath().contains("manifest")) content = bf.toString();
            else content = tokenFormat(bf.toString());
            log.debug("BagFile: " + bf.getFilepath() + "::" + content);
            BagTextPane manifestPane = new BagTextPane(content);
            manifestPaneList.add(manifestPane);
            JScrollPane manifestScrollPane = new JScrollPane();
            manifestScrollPane.setViewportView(manifestPane);
            manifestScrollPane.setToolTipText(parentView.getPropertyMessage("compositePane.tab.manifest.help"));
            manifestScrollPane.setForeground(selectedColor);
            manifestScrollPaneList.add(manifestScrollPane);
            String tabName = bf.getFilepath();
            log.debug("manifestName: " + tabName);
            addTab(tabName, manifestScrollPane);
    	}

    	dataScrollPane = new JScrollPane();
    	String dataContent = defaultBag.getDataContent();
    	dataPane = new BagTextPane(dataContent);
    	dataScrollPane.setViewportView(dataPane);
    	dataScrollPane.setToolTipText(parentView.getPropertyMessage("compositePane.tab.data.help"));
    	dataScrollPane.setForeground(selectedColor);
    	if (!defaultBag.isHoley()) {
            addTab(parentView.getPropertyMessage("compositePane.tab.data"),dataScrollPane);
    	}
        init();
    }
    
    // setBag must be called before updateTabs is called
    public void updateCompositePaneTabs(DefaultBag defaultBag) {
        setBag(defaultBag);
    	if(getComponentCount() > 0){
            removeAll();
            invalidate();
    	}
        populateBagPane();
        int count = getTabCount();
        int selected = getSelectedIndex();
        for (int i = 0; i < count; ++i) {
            Color c = (i == selected) ? unselectedColor : selectedColor;
            setBackgroundAt(i,c);
            setForegroundAt(i,c);
        }
        invalidate();
    }
    
    private String tokenFormat(String content) {
    	StringBuilder buffer = new StringBuilder();    	
        StringTokenizer st = new StringTokenizer(content,",", false);
        while(st.hasMoreTokens()) {
            String s=st.nextToken();
            buffer.append(s);
            buffer.append('\n');
        }
    	return buffer.toString();
    }
}