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
    public static void setupMainGUIold(Player player)
    {
        //do something
        GuiPanel pMainPanel = createMainPanel();

        GuiLabel lPageTitleL = createPageTitle();
        GuiLabel lPageTitleR = createPageTitle();
        GuiLabel lMenuList = createMenuList();
        GuiLabel lPageBodyL = createPageBody();
        GuiLabel lPageBodyR = createPageBody();

        GuiImage iJournal = createJournal();
        GuiImage iJournal_Left = createJournal_Left();
        GuiImage iNavLeft = createNavLeft();
        GuiImage iNavRight = createNavRight();
        GuiImage iNavUp = createNavUp();
        GuiImage iButtonClose = createButtonClose();

        //if your element is a label or child to the panel you also need to call the .addChild Method here e.g.
        pMainPanel.addChild(iJournal);
        pMainPanel.addChild(iJournal_Left);
        iJournal.addChild(lPageTitleL);
        iJournal.addChild(lPageTitleR);
        iJournal_Left.addChild(lMenuList);
        iJournal.addChild(lPageBodyL);
        iJournal.addChild(lPageBodyR);
        iJournal.addChild(iNavLeft);
        iJournal.addChild(iNavRight);
        iJournal.addChild(iNavUp);
        iJournal.addChild(iButtonClose);

        player.setAttribute("pMainPanel", pMainPanel);
        player.setAttribute("lPageTitleL", lPageTitleL);
        player.setAttribute("lPageTitleR", lPageTitleR);
        player.setAttribute("lMenuList", lMenuList);
        player.setAttribute("lPageBodyL", lPageBodyL);
        player.setAttribute("lPageBodyR", lPageBodyR);
        player.setAttribute("iJournal", iJournal);
        player.setAttribute("iJournal_Left", iJournal_Left);
        player.setAttribute("iNavLeft", iNavLeft);
        player.setAttribute("iNavRight", iNavRight);
        player.setAttribute("iNavUp", iNavUp);
        player.setAttribute("iButtonClose", iButtonClose);

        player.addGuiElement((GuiPanel) player.getAttribute("pMainPanel"));
        player.addGuiElement((GuiLabel) player.getAttribute("lPageTitleL"));
        player.addGuiElement((GuiLabel) player.getAttribute("lPageTitleR"));
        player.addGuiElement((GuiLabel) player.getAttribute("lMenuList"));
        player.addGuiElement((GuiLabel) player.getAttribute("lPageBodyL"));
        player.addGuiElement((GuiLabel) player.getAttribute("lPageBodyR"));
        player.addGuiElement((GuiImage) player.getAttribute("iJournal"));
        player.addGuiElement((GuiImage) player.getAttribute("iJournal_Left"));
        player.addGuiElement((GuiImage) player.getAttribute("iNavLeft"));
        player.addGuiElement((GuiImage) player.getAttribute("iNavRight"));
        player.addGuiElement((GuiImage) player.getAttribute("iNavUp"));
        player.addGuiElement((GuiImage) player.getAttribute("iButtonClose"));
    }

    // Some vars to help with gui element positioning. This is doing my head in.
    private static Float LeftM = (1.0f - Float.valueOf(c.getProperty("guiSIZE_X"))) / 2f;
    private static Float BottomM = (1.0f - Float.valueOf(c.getProperty("guiSIZE_Y"))) / 2f;
    private static Float GuiWidth = Float.valueOf(c.getProperty("guiSIZE_X"));
    private static Float GuiHeight = Float.valueOf(c.getProperty("guiSIZE_Y"));
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
        element.setColor(0x00000000);
        element.setBorderColor(0x00000000);
        element.setBorderThickness(0, false);
        element.setVisible(false);
        return element;
    }

    private static GuiLabel createPageTitle(){
        GuiLabel element = new GuiLabel(c.getProperty(""),
                0.10f,
                0.92f,
                true);
        element.setFontSize(15);
        element.setFontColor(0x302013FF);
        element.setColor(0xFF070700); //red
        element.setVisible(false);
        return element;
    }

    private static GuiLabel createMenuList(){
        GuiLabel element = new GuiLabel(c.getProperty("Menu"),
                GuiWidth * 0.15f,
                0.9f,
                true);
        element.setPivot(PivotPosition.TopLeft);
        element.setFontSize(40);
        //element.setFontColor(Long.decode(c.getProperty("guiFontColor")).intValue());
        element.setFontColor(0x302013FF);
        element.setColor(0x07FF0700); //green
        element.setClickable(true);
        element.setVisible(false);
        return element;
    }

    private static GuiLabel createPageBody(){
        GuiLabel element = new GuiLabel(c.getProperty("default text"),
                GuiWidth * 0.65f ,
                0.9f,
                true);
        element.setPivot(PivotPosition.TopLeft);
        element.setFontSize(22);
        //element.setFontColor(Long.decode(c.getProperty("guiFontColor")).intValue());
        element.setFontColor(0x302013FF);
        element.setColor(0x0707FF00);
        element.setVisible(false);
        return element;
    }

    private static GuiImage createNavLeft(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageNavLeft"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageNavLeft"));
        GuiImage element = new GuiImage(ii,
                0.02f,
                0.05f,
                true,
                0.15f,
                0.1f,
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
                0.05f,
                true,
                0.15f,
                0.1f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createNavUp(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageNavRight"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageNavUp"));
        GuiImage element = new GuiImage(ii,
                0.48f,
                0.05f,
                true,
                0.07f,
                0.2f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createButtonClose(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonClose"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("imageButtonClose"));
        GuiImage element = new GuiImage(ii,
                0.91f,
                0.9f,
                true,
                0.06f,
                0.1f,
                true);
        element.setVisible(false);
        element.setClickable(true);
        return element;
    }

    private static GuiImage createJournal(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("bgImageJournal"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("bgImageJournal"));
        GuiImage element = new GuiImage(ii,
                0.0f,
                0.0f,
                true,
                1.0f,
                1.0f,
                true);
        element.setVisible(false);
        element.setClickable(false);
        return element;
    }

    private static GuiImage createJournal_Left(){
        System.out.println("rwdoc Debug: path = " + c.getProperty("rwdocImgLoc") + c.getProperty("bgImageJournal_Left"));
        ImageInformation ii = new ImageInformation(c.getProperty("rwdocImgLoc") + c.getProperty("bgImageJournal_Left"));
        GuiImage element = new GuiImage(ii,
                0.0f,
                0.0f,
                true,
                0.5f,
                1.0f,
                true);
        element.setVisible(false);
        element.setClickable(false);
        return element;
    }

    protected static void toggleGUI(Player player, boolean value)
    {
        GuiPanel pMainPanel = (GuiPanel) player.getAttribute("pMainPanel");
        GuiLabel lPageTitleL = (GuiLabel) player.getAttribute("lPageTitleL");
        GuiLabel lPageTitleR = (GuiLabel) player.getAttribute("lPageTitleR");
        GuiLabel lMenuList = (GuiLabel) player.getAttribute("lMenuList");
        GuiLabel lPageBodyL = (GuiLabel) player.getAttribute("lPageBodyL");
        GuiLabel lPageBodyR = (GuiLabel) player.getAttribute("lPageBodyR");
        GuiImage iJournal = (GuiImage) player.getAttribute("iJournal");
        GuiImage iJournal_Left = (GuiImage) player.getAttribute("iJournal_Left");
        GuiImage iNavLeft = (GuiImage) player.getAttribute("iNavLeft");
        GuiImage iNavRight = (GuiImage) player.getAttribute("iNavRight");
        GuiImage iNavUp = (GuiImage) player.getAttribute("iNavUp");
        GuiImage iButtonClose = (GuiImage) player.getAttribute("iButtonClose");

        pMainPanel.setVisible(value);
        lPageTitleL.setVisible(value);
        lPageTitleR.setVisible(value);
        lMenuList.setVisible(value);
        lPageBodyL.setVisible(value);
        lPageBodyR.setVisible(value);
        iJournal.setVisible(value);
        iJournal_Left.setVisible(value);
        iNavLeft.setVisible(value);
        iNavRight.setVisible(value);
        iNavUp.setVisible(value);
        iButtonClose.setVisible(value);

        player.setAttribute("guiVisible", value);
    }

    protected static void toggleFrontPage(Player player, boolean value)
    {
        GuiImage iJournal_Left = (GuiImage) player.getAttribute("iJournal_Left");
        iJournal_Left.setVisible(value);

        player.setAttribute("frontPageVisible", value);
    }
}