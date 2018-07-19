package com.trevorjd.rwplugin;

import net.risingworld.api.gui.GuiImage;
import net.risingworld.api.gui.GuiLabel;
import net.risingworld.api.gui.GuiPanel;
import net.risingworld.api.gui.PivotPosition;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.ImageInformation;

import static com.trevorjd.rwplugin.rwdoc.c;

public class mainGUI
{
    public static void setupMainGUI(Player player)
    {
        //do something
        GuiPanel pMainPanel = createMainPanel();
        GuiPanel pTitlePanel = createTitlePanel();
        GuiPanel pMenuPanel = createMenuPanel();
        GuiPanel pTextPanel = createTextPanel();
        GuiPanel pFooterPanel = createFooterPanel();
        GuiPanel pSideBar = createSideBar();

        GuiLabel pMainTitle = createTitleLabel();
        GuiLabel pMenuLabel = createMenuLabel();
        GuiLabel pTextLabel = createTextLabel();

        GuiImage pNavLeft = createNavLeft();
        GuiImage pNavUp = createNavUp();
        GuiImage pNavRight = createNavRight();
        GuiImage pButtonUp = createButtonUp();
        GuiImage pButtonDown = createButtonDown();
        GuiImage pButtonClose = createButtonClose();
        GuiImage pButtonFontScaleUp = createButtonFontScaleUp();
        GuiImage pButtonFontScaleDown = createButtonFontScaleDown();

        //if your element is a label or child to the panel you also need to call the .addChild Method here e.g.
        pMenuPanel.addChild(pMenuLabel);

        pTextPanel.addChild(pTextLabel);

        pTitlePanel.addChild(pMainTitle);
        pTitlePanel.addChild(pButtonClose);
        pTitlePanel.addChild(pButtonFontScaleUp);
        pTitlePanel.addChild(pButtonFontScaleDown);

        pFooterPanel.addChild(pNavLeft);
        pFooterPanel.addChild(pNavUp);
        pFooterPanel.addChild(pNavRight);

        pSideBar.addChild(pButtonUp);
        pSideBar.addChild(pButtonDown);


        player.setAttribute("pMainPanel", pMainPanel);
        player.setAttribute("pTitlePanel", pTitlePanel);
        player.setAttribute("pMenuPanel", pMenuPanel);
        player.setAttribute("pTextPanel", pTextPanel);
        player.setAttribute("pFooterPanel", pFooterPanel);
        player.setAttribute("pSideBar", pSideBar);
        player.setAttribute("pMainTitle", pMainTitle);
        player.setAttribute("pMenuLabel", pMenuLabel);
        player.setAttribute("pTextLabel", pTextLabel);
        player.setAttribute("pNavLeft", pNavLeft);
        player.setAttribute("pNavUp", pNavUp);
        player.setAttribute("pNavRight", pNavRight);
        player.setAttribute("pButtonUp", pButtonUp);
        player.setAttribute("pButtonDown", pButtonDown);
        player.setAttribute("pButtonClose", pButtonClose);
        player.setAttribute("pButtonFontScaleUp", pButtonFontScaleUp);
        player.setAttribute("pButtonFontScaleDown", pButtonFontScaleDown);

        player.addGuiElement((GuiPanel) player.getAttribute("pMainPanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("pTitlePanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("pMenuPanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("pTextPanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("pFooterPanel"));
        player.addGuiElement((GuiPanel) player.getAttribute("pSideBar"));
        player.addGuiElement((GuiLabel) player.getAttribute("pMainTitle"));
        player.addGuiElement((GuiLabel) player.getAttribute("pMenuLabel"));
        player.addGuiElement((GuiLabel) player.getAttribute("pTextLabel"));
        player.addGuiElement((GuiImage) player.getAttribute("pNavLeft"));
        player.addGuiElement((GuiImage) player.getAttribute("pNavUp"));
        player.addGuiElement((GuiImage) player.getAttribute("pNavRight"));
        player.addGuiElement((GuiImage) player.getAttribute("pButtonUp"));
        player.addGuiElement((GuiImage) player.getAttribute("pButtonDown"));
        player.addGuiElement((GuiImage) player.getAttribute("pButtonClose"));
        player.addGuiElement((GuiImage) player.getAttribute("pButtonFontScaleUp"));
        player.addGuiElement((GuiImage) player.getAttribute("pButtonFontScaleDown"));
    }

    private static int scaleFont(int fontsize)
    {
        double scaled = fontsize * Double.valueOf(c.getProperty("guiSIZE_Y"));
        return (int) scaled;
    }

    // Some vars to help with gui element positioning. This is doing my head in.
    private static Float LeftM = Float.valueOf(c.getProperty("guiSIZE_X")) / 2f; // half the gui width
    private static Float BottomM = Float.valueOf(c.getProperty("guiSIZE_Y")) / 2f; // half the gui height
    private static Float GuiWidth = Float.valueOf(c.getProperty("guiSIZE_X"));
    private static Float GuiHeight = Float.valueOf(c.getProperty("guiSIZE_Y"));
    private static Float TitleWidth = GuiWidth;
    private static Float TitleHeight = Float.valueOf(c.getProperty("guiSIZE_Y")) * 0.1f; // 10% of gui height
    private static Float FooterWidth = GuiWidth;
    private static Float FooterHeight = Float.valueOf(c.getProperty("guiSIZE_Y")) * 0.1f; // 10% of gui height
    private static Float SidebarWidth = Float.valueOf(c.getProperty("guiSIZE_X")) * 0.05f; // 5% of gui width
    private static Float SidebarHeight = GuiHeight - TitleHeight - FooterHeight;
    private static Float MenuWidth = Float.valueOf(c.getProperty("guiSIZE_X")) * 0.30f; // 20% of gui width
    private static Float TextWidth = Float.valueOf(c.getProperty("guiSIZE_X")) * 0.65f; // 20% of gui width
    private static Float TextPadding = 0.05f;
    private static int guiBgColor = 0x30201370;

    private static GuiPanel createMainPanel()
    {
        GuiPanel element = new GuiPanel(
                LeftM,
                BottomM,
                true,
                GuiWidth,
                GuiHeight,
                true);
        element.setColor(guiBgColor);
        element.setBorderColor(0x6e6259FF); //red
        element.setBorderThickness(5, false);
        element.setVisible(false);
        return element;
    }

    private static GuiPanel createTitlePanel()
    {
        GuiPanel element = new GuiPanel(
                LeftM,
                BottomM + GuiHeight - TitleHeight,
                true,
                TitleWidth,
                TitleHeight,
                true);
        element.setColor(guiBgColor);
        //element.setBorderColor(0xFF0000FF); //red
        //element.setBorderThickness(1, false);
        element.setVisible(false);
        return element;
    }


    private static GuiPanel createFooterPanel()
    {
        GuiPanel element = new GuiPanel(
                LeftM,
                BottomM,
                true,
                FooterWidth,
                FooterHeight,
                true);
        element.setColor(guiBgColor);
        //element.setBorderColor(0xFFFF00FF); //yellow
        //element.setBorderThickness(1, false);
        element.setVisible(false);
        return element;
    }

    private static GuiPanel createMenuPanel()
    {
        GuiPanel element = new GuiPanel(
                LeftM,
                BottomM + FooterHeight,
                true,
                MenuWidth,
                GuiHeight - FooterHeight - TitleHeight,
                true);
        element.setColor(guiBgColor);
        //element.setBorderColor(0x00FF00FF); // green
        //element.setBorderThickness(1, false);
        element.setVisible(false);
        return element;
    }

    private static GuiPanel createTextPanel()
    {
        GuiPanel element = new GuiPanel(
                LeftM + MenuWidth,
                BottomM + FooterHeight,
                true,
                TextWidth,
                GuiHeight - FooterHeight - TitleHeight,
                true);
        element.setColor(guiBgColor);
        //element.setBorderColor(0x0000FFFF); //blue
        //element.setBorderThickness(1, false);
        element.setVisible(false);
        return element;
    }

    private static GuiPanel createSideBar()
    {
        GuiPanel element = new GuiPanel(
                LeftM + GuiWidth - SidebarWidth,
                BottomM,
                true,
                SidebarWidth,
                SidebarHeight,
                true);
        element.setColor(guiBgColor);
        //element.setBorderColor(0x00FFFFFF); // cyan
        //element.setBorderThickness(1, false);
        element.setVisible(false);
        return element;
    }

    private static GuiLabel createTitleLabel(){
        GuiLabel element = new GuiLabel(c.getProperty("guiTitle"),
                0.25f,
                0.25f,
                true);
        element.setFontSize(20);
        element.setFontColor(0xFFFFFFFF);
        //element.setColor(0x07070790); //grey
        element.setVisible(false);
        return element;
    }

    private static GuiLabel createMenuLabel(){
        GuiLabel element = new GuiLabel(c.getProperty("Menu"),
                TextPadding / 2,
                1.0f - TextPadding,
                true);
        element.setPivot(PivotPosition.TopLeft);
        element.setFontSize(20);
        element.setFontColor(Long.decode(c.getProperty("guiFontColor")).intValue());
        //element.setColor(0x07FF0710);
        element.setClickable(true);
        element.setVisible(false);
        return element;
    }

    private static GuiLabel createTextLabel(){
        GuiLabel element = new GuiLabel(c.getProperty("Text me, baby!"),
                TextPadding / 2 ,
                1.0f - TextPadding,
                true);
        element.setPivot(PivotPosition.TopLeft);
        element.setFontSize(20);
        element.setFontColor(Long.decode(c.getProperty("guiFontColor")).intValue());
        //element.setFontColor(0xFFFFFFFF);
        //element.setColor(0x0707FF10);
        element.setVisible(false);
        return element;
    }

    private static GuiImage createNavLeft(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageNavLeft"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageNavLeft"));
        GuiImage element = new GuiImage(ii,
                0.1f,
                0.1f,
                true,
                0.1f,
                0.8f,
                true);
        element.setClickable(true);
        element.setVisible(false);
        return element;
    }

    private static GuiImage createNavRight(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageNavRight"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageNavRight"));
        GuiImage element = new GuiImage(ii,
                0.85f,
                0.1f,
                true,
                0.1f,
                0.8f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createNavUp(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageNavUp"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageNavUp"));
        GuiImage element = new GuiImage(ii,
                0.5f,
                0.1f,
                true,
                0.1f,
                0.8f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createButtonUp(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonUp"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonUp"));
        GuiImage element = new GuiImage(ii,
                0.1f,
                0.8f,
                true,
                0.8f,
                0.06f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createButtonDown(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonDown"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonDown"));
        GuiImage element = new GuiImage(ii,
                0.1f,
                0.15f,
                true,
                0.8f,
                0.06f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createButtonClose(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonClose"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonClose"));
        GuiImage element = new GuiImage(ii,
                0.95f,
                0.2f,
                true,
                0.04f,
                0.6f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createButtonFontScaleUp(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonFontScaleUp"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonFontScaleUp"));
        GuiImage element = new GuiImage(ii,
                0.89f,
                0.2f,
                true,
                0.05f,
                0.6f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createButtonFontScaleDown(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonFontScaleDown"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonFontScaleDown"));
        GuiImage element = new GuiImage(ii,
                0.83f,
                0.2f,
                true,
                0.05f,
                0.6f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    protected static void showHideGUI(Player player, boolean value)
    {
        System.out.println("rwdoc Debug: GUI visible: " + value);
        GuiPanel pMainPanel = (GuiPanel) player.getAttribute("pMainPanel");
        GuiPanel pTitlePanel = (GuiPanel) player.getAttribute("pTitlePanel");
        GuiPanel pMenuPanel = (GuiPanel) player.getAttribute("pMenuPanel");
        GuiPanel pTextPanel = (GuiPanel) player.getAttribute("pTextPanel");
        GuiPanel pFooterPanel = (GuiPanel) player.getAttribute("pFooterPanel");
        GuiPanel pSideBar = (GuiPanel) player.getAttribute("pSideBar");

        GuiLabel pMainTitle = (GuiLabel) player.getAttribute("pMainTitle");
        GuiImage pButtonFontScaleUp = (GuiImage) player.getAttribute("pButtonFontScaleUp");
        GuiImage pButtonFontScaleDown = (GuiImage) player.getAttribute("pButtonFontScaleDown");

        GuiLabel pMenuLabel = (GuiLabel) player.getAttribute("pMenuLabel");
        GuiLabel pTextLabel = (GuiLabel) player.getAttribute("pTextLabel");

        GuiImage pNavLeft = (GuiImage) player.getAttribute("pNavLeft");
        GuiImage pNavUp = (GuiImage) player.getAttribute("pNavUp");
        GuiImage pNavRight = (GuiImage) player.getAttribute("pNavRight");

        GuiImage pButtonUp = (GuiImage) player.getAttribute("pButtonUp");
        GuiImage pButtonDown = (GuiImage) player.getAttribute("pButtonDown");
        GuiImage pButtonClose = (GuiImage) player.getAttribute("pButtonClose");

        pMainPanel.setVisible(value);
        pTitlePanel.setVisible(value);
        pMenuPanel.setVisible(value);
        pTextPanel.setVisible(value);
        pFooterPanel.setVisible(value);
        pSideBar.setVisible(value);

        pMainTitle.setVisible(value);
        pButtonClose.setVisible(value);
        pButtonFontScaleUp.setVisible(value);
        pButtonFontScaleDown.setVisible(value);

        pMenuLabel.setVisible(value);
        pTextLabel.setVisible(value);

        pNavLeft.setVisible(value);
        pNavUp.setVisible(value);
        pNavRight.setVisible(value);

        pButtonUp.setVisible(value);
        pButtonDown.setVisible(value);

        player.setAttribute("guiVisible", value);
    }

    protected static void refreshDisplay(Player player)
    {
        System.out.println("rwdoc Debug: Refreshing display.");
        GuiLabel guiLabel = (GuiLabel) player.getAttribute("pTextLabel");
        String string = guiLabel.getText();
        int fontSize = (int) player.getAttribute("FontSize");
        // rwdocUtils.stoneFontSize(player);
        guiLabel.setFontSize(fontSize);
        guiLabel.setText(rwdocUtils.wordWrap(rwdocUtils.stripNewlines(string)));
    }
}