
public class Question implements Comparable<Question>
{

/*
 * Question Objects - can be created from a file reader
 *
*/

	public String question; 
	public String[] choices;
	public int answer;
	public int question_id;

	Question()
	{

	}

	Question(String q, String[] c, int a, int q_id)
	{
		question = q; 
		
		choices = new String[c.length];

		for(int i=0; i<c.length; i++)
			choices[i] = c[i];

		answer = a; 
		
		question_id = q_id;
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
	
	public String choicesToString()
	{
		return "A: "+choices[0]+"\nB: "+choices[1]+"\nC: "+choices[2]+"\nD: "+choices[3];
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
    	
    	/*
    	if(!question.equals(q.question))
    		return AFTER;
    	else return EQUAL;
    	*/
    	
    	if(question_id == q.question_id)
    		return EQUAL;
    	else if(question_id < q.question_id)
    		return AFTER;
    	else return BEFORE;
 	}

}
