import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.lang.Object.*;
import java.util.*;
import java.applet.Applet;
import java.io.*;
import java.net.*;

public class GrandTour extends Applet implements ActionListener, ItemListener
{
	RunTour tour;

	final int WIDTH=500;
	final int HEIGHT=590;

	TextField axisEJ = new TextField("0.0", 4);
	TextField axisJS = new TextField("0.0", 4);
	TextField axisSU = new TextField("0.0", 4);
	TextField axisUN = new TextField("0.0", 4);

	TextField eccEJ = new TextField("0.0", 4);
	TextField eccJS = new TextField("0.0", 4);
	TextField eccSU = new TextField("0.0", 4);
	TextField eccUN = new TextField("0.0", 4);

	TextField phiJ = new TextField("0.0", 4);
	TextField phiS = new TextField("0.0", 4);
	TextField phiU = new TextField("0.0", 4);
	TextField phiN = new TextField("0.0", 4);

 	Button launch = new Button("Launch");
    Button stop = new Button("Stop");
	Button reset = new Button("Reset");

	Checkbox seeVoy = new Checkbox("Voyager");

	CheckInput check = new CheckInput();

	/* function init contains descriptions of layout and initialization of objects
	*****************************************************************************************/
    public void init()
    {
	    Image earth = getImage(getCodeBase(), "earthsmall.gif");
	    Image jupiter = getImage(getCodeBase(), "jupiter.jpg");
	    Image saturn = getImage(getCodeBase(), "Saturn.jpg");
	    Image uranus = getImage(getCodeBase(), "uranus.gif");
	    Image neptune = getImage(getCodeBase(), "uranus.gif");
        Image sun = getImage(getCodeBase(), "sunsmall.gif");
		Image successJupiter = getImage(getCodeBase(), "JupiterBig.jpg");
		Image successSaturn = getImage(getCodeBase(), "SaturnBig.jpg");
		Image successUranus = getImage(getCodeBase(), "UranusBig.jpg");
		Image successNeptune = getImage(getCodeBase(), "NeptuneBig.jpg");
		Image failure = getImage(getCodeBase(), "failure2.jpg");

		setLayout(new BorderLayout());
		setFont(new Font("Times New Roman",Font.PLAIN,10));

		axisEJ.setBackground(Color.white);
		axisEJ.setForeground(Color.black);
		axisEJ.setEditable(true);
		axisEJ.isFocusTraversable();
		axisEJ.addKeyListener(check);

		axisJS.setBackground(Color.white);
		axisJS.setForeground(Color.black);
		axisJS.setEditable(true);
		axisJS.isFocusTraversable();
		axisJS.addKeyListener(check);

		axisSU.setBackground(Color.white);
		axisSU.setForeground(Color.black);
		axisSU.setEditable(true);
		axisSU.isFocusTraversable();
		axisSU.addKeyListener(check);

		axisUN.setBackground(Color.white);
		axisUN.setForeground(Color.black);
		axisUN.setEditable(true);
		axisUN.isFocusTraversable();
		axisUN.addKeyListener(check);

		eccEJ.setBackground(Color.white);
		eccEJ.setForeground(Color.black);
		eccEJ.setEditable(true);
		eccEJ.isFocusTraversable();
		eccEJ.addKeyListener(check);

		eccJS.setBackground(Color.white);
		eccJS.setForeground(Color.black);
		eccJS.setEditable(true);
		eccJS.isFocusTraversable();
		eccJS.addKeyListener(check);

		eccSU.setBackground(Color.white);
		eccSU.setForeground(Color.black);
		eccSU.setEditable(true);
		eccSU.isFocusTraversable();
		eccSU.addKeyListener(check);

		eccUN.setBackground(Color.white);
		eccUN.setForeground(Color.black);
		eccUN.setEditable(true);
		eccUN.isFocusTraversable();
		eccUN.addKeyListener(check);

		phiJ.setBackground(Color.white);
		phiJ.setForeground(Color.black);
		phiJ.setEditable(true);
		phiJ.isFocusTraversable();
		phiJ.addKeyListener(check);

		phiS.setBackground(Color.white);
		phiS.setForeground(Color.black);
		phiS.setEditable(true);
		phiS.isFocusTraversable();
		phiS.addKeyListener(check);

		phiU.setBackground(Color.white);
		phiU.setForeground(Color.black);
		phiU.setEditable(true);
		phiU.isFocusTraversable();
		phiU.addKeyListener(check);

		phiN.setBackground(Color.white);
		phiN.setForeground(Color.black);
		phiN.setEditable(true);
		phiN.isFocusTraversable();
		phiN.addKeyListener(check);

		launch.setBackground(Color.lightGray);
		launch.setForeground(Color.black);
	    launch.addActionListener(this);

		stop.setBackground(Color.lightGray);
		stop.setForeground(Color.black);
	    stop.addActionListener(this);

		reset.setBackground(Color.lightGray);
		reset.setForeground(Color.black);
		reset.addActionListener(this);

		seeVoy.addItemListener(this);
		seeVoy.setBackground(Color.black);
		seeVoy.setForeground(Color.white);

		/*	creation and loading of panels
     	****************************************************/
     	Panel aePanel = new Panel(new GridLayout(4, 1));
     	aePanel.setBackground(Color.black);

		Panel EJPanel = new Panel(new GridLayout(1, 5));
		Panel JSPanel = new Panel(new GridLayout(1, 5));
		Panel SUPanel = new Panel(new GridLayout(1, 5));
		Panel UNPanel = new Panel(new GridLayout(1, 5));

		EJPanel.setBackground(Color.red);
		EJPanel.setForeground(Color.black);
		EJPanel.setFont(new Font("Roman",Font.BOLD,11));
		EJPanel.add(new Label(" Earth - Jup: "));
		EJPanel.add(new Label("            a:"));
		EJPanel.add(axisEJ);
		EJPanel.add(new Label("            e:"));
		EJPanel.add(eccEJ);

		JSPanel.setBackground(Color.gray);
		JSPanel.setForeground(Color.black);
		JSPanel.setFont(new Font("Roman",Font.BOLD,11));
		JSPanel.add(new Label("   Jup - Sat: "));
		JSPanel.add(new Label("            a:"));
		JSPanel.add(axisJS);
		JSPanel.add(new Label("            e:"));
		JSPanel.add(eccJS);

		SUPanel.setBackground(Color.cyan);
		SUPanel.setForeground(Color.black);
		SUPanel.setFont(new Font("Roman",Font.BOLD,11));
		SUPanel.add(new Label("   Sat - Ura: "));
		SUPanel.add(new Label("            a:"));
		SUPanel.add(axisSU);
		SUPanel.add(new Label("            e:"));
		SUPanel.add(eccSU);

		UNPanel.setBackground(Color.green);
		UNPanel.setForeground(Color.black);
		UNPanel.setFont(new Font("Roman",Font.BOLD,11));
		UNPanel.add(new Label("   Ura - Nep: "));
		UNPanel.add(new Label("            a:"));
		UNPanel.add(axisUN);
		UNPanel.add(new Label("            e:"));
		UNPanel.add(eccUN);

		aePanel.add(EJPanel);
		aePanel.add(JSPanel);
		aePanel.add(SUPanel);
		aePanel.add(UNPanel);

		axisEJ.addActionListener(this);
		axisJS.addActionListener(this);
		axisSU.addActionListener(this);
		axisUN.addActionListener(this);

		eccEJ.addActionListener(this);
		eccJS.addActionListener(this);
		eccSU.addActionListener(this);
		eccUN.addActionListener(this);

		Panel phiPanel = new Panel(new GridLayout(4, 2));
		phiPanel.setBackground(Color.yellow);
		phiPanel.setForeground(Color.black);
		phiPanel.setFont(new Font("Roman",Font.BOLD,11));
		phiPanel.add(new Label("      Jupiter "));
		phiPanel.add(phiJ);
		phiPanel.add(new Label("      Saturn "));
		phiPanel.add(phiS);
		phiPanel.add(new Label("      Uranus "));
		phiPanel.add(phiU);
		phiPanel.add(new Label("      Neptune "));
		phiPanel.add(phiN);

		Panel labelPhiPanel = new Panel(new BorderLayout());
		labelPhiPanel.setBackground(Color.yellow);
		labelPhiPanel.setForeground(Color.black);
		labelPhiPanel.setFont(new Font("Roman",Font.BOLD,11));
		labelPhiPanel.add("North", new Label("    Planet starting angle"));
		labelPhiPanel.add("Center", phiPanel);

		phiJ.addActionListener(this);
		phiS.addActionListener(this);
		phiU.addActionListener(this);
		phiN.addActionListener(this);

		Panel layoutPanel = new Panel(new BorderLayout());
		layoutPanel.add("Center", aePanel);
		layoutPanel.add("East", labelPhiPanel);

		Panel actionPanel = new Panel(new GridLayout(2, 7));
		actionPanel.setBackground(Color.black);
		actionPanel.add(seeVoy);
		actionPanel.add(new Label(" "));
		actionPanel.add(launch);
		actionPanel.add(stop);
		actionPanel.add(reset);
		actionPanel.add(new Label(" "));
		actionPanel.add(new Label(" "));

		actionPanel.add(new Label(" "));
		actionPanel.add(new Label(" "));
		actionPanel.add(new Label(" "));
		actionPanel.add(new Label(" "));
		actionPanel.add(new Label(" "));
		actionPanel.add(new Label(" "));

//700,830
		tour = new RunTour(WIDTH, HEIGHT, axisEJ, axisJS, axisSU, axisUN, eccEJ, eccJS, eccSU, eccUN, phiJ, phiS, phiU, phiN,
		                      successJupiter, successSaturn, successUranus, successNeptune, failure, earth, jupiter, saturn, uranus, neptune, sun);

		/*	screen layout
		*************************/
        add("North", layoutPanel);
        add("Center", tour);
		add("South", actionPanel);

		setSize(WIDTH, HEIGHT);
	}

	/*	updates textfields as new max/min values are entered
	******************************************************************/
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Launch"))
		{
			if(!tour.beenToNeptune)
			{
				tour.go(true);
			}
			else if(tour.beenToNeptune)
			{
				tour.success = false;
				tour.update(tour.g);
			}
		}
		else if(e.getActionCommand().equals("Stop"))
		{
			tour.go(false);
		}
		else if(e.getActionCommand().equals("Reset"))
		{
			tour.reset();
		}
		else
		{
	    	tour.paramSwitch();
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getItem().equals("Voyager")) {
			tour.setVoy(((Checkbox)e.getItemSelectable()).getState());
		}
	}
}



