import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.Object.*;
import java.lang.Character.*;

class CheckInput extends KeyAdapter
{
	public void keyPressed(KeyEvent event)
	{
		TextField maxPosition = (TextField)event.getSource();
		TextField minVelocity = (TextField)event.getSource();
		TextField maxVelocity = (TextField)event.getSource();

		char entry;
		entry = event.getKeyChar();

		if(entry == 'a' || entry == 'b' || entry == 'c' || entry == 'd' || entry == 'e' || entry == 'f' || entry == 'g'
			|| entry == 'h' || entry == 'i' || entry == 'j' || entry == 'k' || entry == 'l' || entry == 'm' || entry == 'n'
			|| entry == 'o' || entry == 'p' || entry == 'q' || entry == 'r' || entry == 's' || entry == 't' || entry == 'u'
			|| entry == 'v' || entry == 'w' || entry == 'x' || entry == 'y' || entry == 'z' || entry == ',' || entry == '<'
			|| entry == '>' || entry == '/' || entry == '?' || entry == ';' || entry == ':' || entry == '[' || entry == ']'
			|| entry == '{' || entry == '}' || entry == '|' || entry == '+' || entry == '=' || entry == '_'
			|| entry == '!' || entry == '@' || entry == '#' || entry == '$' || entry == '%' || entry == '^' || entry == '&'
			|| entry == '*' || entry == '(' || entry == ')' || entry == '`' || entry == '~' || entry == '"')
		{
			event.consume();
			maxPosition.getToolkit().beep();
		}
		else
		{

		}
	}
}