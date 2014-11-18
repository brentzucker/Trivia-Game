
public class Question {
	
	/*
	public String question;
	private String answer_A;
	private String answer_B;
	private String answer_C;
	private String answer_D;
	private char correct_answer;
	
	
	public Question(String[] question_answer_array, char correct_answer){
		
		this.question = question_answer_array[0];
		this.answer_A = question_answer_array[1];
		this.answer_B = question_answer_array[2];
		this.answer_C = question_answer_array[3];
		this.answer_D = question_answer_array[4];
		
		if(correct_answer == 'A' || correct_answer == 'B' || correct_answer == 'C' || correct_answer == 'D'){
			this.correct_answer = correct_answer;
		}
		else
			throw new RuntimeException("Invalid Answer");
		
	}
	
	
	public boolean isCorrect(char answer){
		return answer == this.correct_answer;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getAnswerA(){
		return answer_A;
	}
	
	public String getAnswerB(){
		return answer_B;
	}
	
	public String getAnswerC(){
		return answer_C;
	}
	
	public String getAnswerD(){
		return answer_D;
	}
	*/

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

}
