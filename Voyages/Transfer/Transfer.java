import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.lang.Object.*;
import java.util.*;
import java.applet.Applet;
import java.io.*;
import java.net.*;

public class Transfer extends Applet implements ActionListener
{
	RunSimulation move;

	TextField semimajorAxis = new TextField("0.0", 4);
	TextField eccentricityAxis = new TextField("0.0", 4);
	TextField phi = new TextField("0.0", 4);

 	Button launch = new Button("Launch");
    Button stop = new Button("Stop");
	Button reset = new Button("Reset");

	CheckInput check = new CheckInput();

	/* function init contains descriptions of layout and initialization of objects
	*****************************************************************************************/
    public void init()
    {
	    Image earth = getImage(getCodeBase(), "earth.gif");
	    Image mars = getImage(getCodeBase(), "mars.gif");
        Image sun = getImage(getCodeBase(), "sun.gif");
        Image satellite = getImage(getCodeBase(), "pic1_s.jpg");
		Image success = getImage(getCodeBase(), "success.gif");
		Image failure = getImage(getCodeBase(), "failure.jpg");
		Image crash = getImage(getCodeBase(), "crash.jpg");

		setLayout(new BorderLayout());

		launch.setBackground(Color.lightGray);
		launch.setForeground(Color.black);
	    launch.addActionListener(this);

		stop.setBackground(Color.lightGray);
		stop.setForeground(Color.black);
	    stop.addActionListener(this);

		reset.setBackground(Color.lightGray);
		reset.setForeground(Color.black);
		reset.addActionListener(this);

		semimajorAxis.setBackground(Color.white);
		semimajorAxis.setForeground(Color.black);
		semimajorAxis.setEditable(true);
		semimajorAxis.isFocusTraversable();
		semimajorAxis.addKeyListener(check);

		eccentricityAxis.setBackground(Color.white);
		eccentricityAxis.setForeground(Color.black);
		eccentricityAxis.setEditable(true);
		eccentricityAxis.isFocusTraversable();
		eccentricityAxis.addKeyListener(check);

		phi.setBackground(Color.white);
		phi.setForeground(Color.black);
		phi.setEditable(true);
		phi.isFocusTraversable();
		phi.addKeyListener(check);

		/*	creation and loading of infoPanel
     	****************************************************/
     	Panel infoPanel = new Panel(new GridLayout(3, 3));
     	infoPanel.setBackground(Color.black);
		infoPanel.setForeground(Color.yellow);
		infoPanel.setFont(new Font("Times New Roman",Font.PLAIN,10));

		infoPanel.add(new Label("Satellite"));
		infoPanel.add(new Label("Satellite"));
		infoPanel.add(new Label("Mars"));
		infoPanel.add(new Label("Semi-major Axis (a)"));
		infoPanel.add(new Label("Eccentricity (e)"));
		infoPanel.add(new Label("Phi (degrees)"));
		infoPanel.add(semimajorAxis);
		infoPanel.add(eccentricityAxis);
		infoPanel.add(phi);

		semimajorAxis.addActionListener(this);
		eccentricityAxis.addActionListener(this);
		phi.addActionListener(this);

		Panel actionPanel = new Panel(new GridLayout(2, 7));
		actionPanel.setBackground(Color.black);
		actionPanel.add(new Label(" "));
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

		move = new RunSimulation(500, 630, semimajorAxis, eccentricityAxis, phi, earth, mars, sun, satellite, success, failure, crash);

		/*	screen layout
		*************************/
        add("North", infoPanel);
      	add("Center", move);
		add("South", actionPanel);

		setSize(500, 630);
	}

	/*	updates textfields as new max/min values are entered
	******************************************************************/
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Launch"))
		{
			move.go(true);
		}
		else if(e.getActionCommand().equals("Stop"))
		{
			move.go(false);
		}
		else if(e.getActionCommand().equals("Reset"))
		{
			move.reset();
		}
		else
		{
	    	move.paramSwitch();
		}
	}
}



