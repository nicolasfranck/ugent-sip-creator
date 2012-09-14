package gov.loc.repository.bagger.ui;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;

public class MainView extends AbstractView{
    @Override
    protected JComponent createControl() {
        JPanel bagViewPanel = new JPanel(new BorderLayout(2, 2));        
        bagViewPanel.add(BagView.getInstance().infoInputPane);
        return bagViewPanel;
    }
    @Override
     protected void registerLocalCommandExecutors(PageComponentContext context) {
        BagView bagView = BagView.getInstance();
        context.register("startCommand",bagView.startExecutor);
        context.register("openCommand",bagView.openExecutor);
        context.register("createBagInPlaceCommand",bagView.createBagInPlaceExecutor);
        context.register("clearCommand",bagView.clearExecutor);
        context.register("validateCommand",bagView.validateExecutor);
        context.register("completeCommand",bagView.completeExecutor);
        context.register("addDataCommand",bagView.addDataExecutor);
        context.register("saveBagCommand",bagView.saveBagExecutor);
        context.register("saveBagAsCommand",bagView.saveBagAsExecutor);
     }
}