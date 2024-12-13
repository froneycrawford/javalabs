public class Orbit
{
    double a, e, tau, period;

    /*  Constructors for Orbit
    **************************************************/
    Orbit(double ain, double ein, double tauin)
    {
        a = ain;
        e = ein;
        tau = tauin;
        period = Math.pow(a,1.5);
    }

    Orbit(double ain, double ein)
    {
        a = ain;
        e = ein;
        tau = 0.0;
        period = Math.pow(a, 1.5);
    }

    Orbit()
    {
        a = 1.0;
        e = 0.0;
        tau = 0.0;
        period = Math.pow(a, 1.5);
    }

    /*  function orbitPosition
    ******************************************************/
    public double[] orbitPosition(double time)
    {
        double[] pos = new double[2];
        double M, bigE, r, tanfon2, f;

        M = (2.0 * Math.PI/period) * (time-tau);
        bigE = solveKepler(M);
        r = a * (1-e * Math.cos(bigE));
        tanfon2 = Math.sqrt((1+e)/(1-e)) * Math.tan(bigE/2.0);
        f = 2.0 * Math.atan(tanfon2);
        pos[0] = r * Math.cos(f);
        pos[1] = r * Math.sin(f);

        return pos;
    }

    /*  function round rounds a double to one decimal place
    ***************************************************************************/
    public double round(double r)
    {
        r *= 10;
        r = Math.round(r);
        r /= 10;
        return r;
    }

    /*  function solveKepler
    ******************************************************/
    public double solveKepler(double M)
    {
        double diff = 1.0;
        double bigE, bigEnew;

        bigE = M;

        while(diff > 0.0001)
        {
            bigEnew = bigE + (M-bigE+e * Math.sin(bigE))/(1-e * Math.cos(bigE));
            diff = Math.abs((bigEnew-bigE)/bigE);
            bigE = bigEnew;
        }
        return bigE;
    }

    /*  function updateOrbit
    ******************************************************/
    public void updateOrbit(double ain, double ein, double phiin)
    {
        double phi;

        a = ain;
        e = ein;
        phi = phiin;
        period = Math.pow(a, 1.5);
        tau = -period * phi / 360.0;
    }

}
