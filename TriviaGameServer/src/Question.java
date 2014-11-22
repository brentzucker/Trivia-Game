
public class Question implements Comparable<Question> 
{

/*
 * Question Objects - can be created from a file reader
 *
*/

	public String question; 
	public String[] choices;
	public int answer;

	Question()
	{

	}

	Question(String q, String[] c, int a)
	{
		question = q; 
		
		choices = new String[c.length];

		for(int i=0; i<c.length; i++)
			choices[i] = c[i];

		answer = a; 
	}

	Question(String q)
	{
		question = q;
	}

	Question(String[] c)
	{
		choices = new String[c.length];

		for(int i=0; i<c.length; i++)
			choices[i] = c[i];
	}

	Question(char a)
	{
		answer = a; 
	}

	public boolean isCorrect(char a)
	{
		int ans; 

		a = Character.toUpperCase(a);

		if(a == 'A')
		{
			ans = 0;
		}
		else if(a == 'B')
		{
			ans = 1; 
		}
		else if(a == 'C')
		{
			ans = 2; 
		}
		else 
			ans = 3; 

		if(choices[answer].equals(choices[ans]))
		{
			return true;
		}
		else return false;
	}

	public int compareTo(Question q)
 	{
 		final int BEFORE = -1;
    	final int EQUAL = 0;
    	final int AFTER = 1;

    	if(question.equals(q.question))
    		return EQUAL;

    	return AFTER;
 	}

}
