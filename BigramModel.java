package il.ac.tau.cs.sw1.ex5;
import java.io.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class BigramModel {
	public static final int MAX_VOCABULARY_SIZE = 14500;
	public static final String VOC_FILE_SUFFIX = ".voc";
	public static final String COUNTS_FILE_SUFFIX = ".counts";
	public static final String SOME_NUM = "some_num";
	public static final int ELEMENT_NOT_FOUND = -1;
	
	String[] mVocabulary;
	int[][] mBigramCounts;
	
	// DO NOT CHANGE THIS !!! 
	public void initModel(String fileName) throws IOException
	{
		mVocabulary = buildVocabularyIndex(fileName);
		mBigramCounts = buildCountsArray(fileName, mVocabulary);
		
	}
	
	
	
	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public String[] buildVocabularyIndex(String fileName) throws IOException
	{
		String[] vocabulary=new String[MAX_VOCABULARY_SIZE];
		
		String NUMBERS="123456789";
		String ALPHABET="abcdefghijklmnopqrstuvwxyz";
		String[] finalvoc;
		
		try
		{
		
		BufferedReader bf=new BufferedReader(new FileReader(fileName));
		String BFR=bf.readLine();
		
		boolean flag=false;
		int  i=0;
		int crt=0;
		
		while(BFR!=null&&crt<MAX_VOCABULARY_SIZE)
		{
			BFR=BFR.toLowerCase();
			String [] Line=BFR.split(" ");
			
			for(int z=0;z<Line.length;z++)
			{
				String word=Line[z];
				int n=word.length();
				
				int NUM_crt=0;
				
				for(int a=0;a<n;a++)
				{
					char abcde=Line[z].charAt(a);
					if(NUMBERS.indexOf(abcde)!=ELEMENT_NOT_FOUND) 
					{
						NUM_crt++;
					}
					if(ALPHABET.indexOf(abcde)!=ELEMENT_NOT_FOUND)
					{
						flag=true;
					}
				}
				if(flag)
				{
					String Curr_word=Line[z];
					if(indexa(word,vocabulary,i)==ELEMENT_NOT_FOUND)
					{
						vocabulary[i]=Curr_word;
						i++;
					}
				}
				if(NUM_crt==n)
				{
					vocabulary[i]=SOME_NUM;
					i++;
				}
				flag=false;
			}
			BFR=bf.readLine();
		}
		bf.close();
		
		finalvoc=Arrays.copyOf(vocabulary,i);
		}		
		finally
		{
			
		}
		
		
		return finalvoc;
	}
	
	public int indexa(String word,String []vocabulary,int i)
	{
		
		for(int x=0;x<i;x++)
		{
			if(vocabulary[x]!=null)
			{
			if(vocabulary[x].equals(word))
				return x;}
			else
				break;
		}
		return ELEMENT_NOT_FOUND;
	}
	
	
	public boolean instruct1(String word)
	{
		word=word.toLowerCase();
		for(int i=0;i<word.length();i++)
		{
			int asc=(int)(word.charAt(i));
			if((((asc>=97)&&(asc<=122))))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean instruct2(String word)
	{
		for(int z=0;z<word.length();z++)
		{
			if(!Character.isDigit(word.charAt(z)))
			{
				return false;
			}
		}
		return true;
	}
	
	
	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public int[][] buildCountsArray(String fileName, String[] vocabulary) throws IOException
	{ 
		int a=vocabulary.length;
		int Counts[][]=new int[a][a];
		
		try 
		{
			BufferedReader COUNTS_BF=new BufferedReader(new FileReader(fileName));
			String BFR=COUNTS_BF.readLine();
			
			while(BFR!=null) 
			{
				String[] LINES=BFR.split(" ");
				for(int i=1;i<LINES.length;i++)
				{
					String A1=LINES[i-1].toLowerCase();
					String A2=LINES[i].toLowerCase();
					
					if(Arrays.asList(vocabulary).contains(A1)&&Arrays.asList(vocabulary).contains(A2))
					{
						Counts[Arrays.asList(vocabulary).indexOf(A1)][Arrays.asList(vocabulary).indexOf(A2)]++;
					}
				}
				BFR=COUNTS_BF.readLine();
				
			}
			COUNTS_BF.close();
		}
		finally
		{
			
		}
		return Counts;
	}
	
	
	
	/*
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: fileName is a legal file path
	 */
	public void saveModel(String fileName) throws IOException
	{ 
		File vocf=new File(fileName+VOC_FILE_SUFFIX);
		File countsf=new File(fileName+COUNTS_FILE_SUFFIX);
		
		 FileWriter vocWriter = new FileWriter(vocf );
		 vocWriter.write(mVocabulary.length);
		 for(int i=0;i<mVocabulary.length;i++)
		 {
			 vocWriter.write("\r\n" + i +","+mVocabulary[i]);
		 }
		 vocWriter.close();
		 
		 FileWriter countWriter=new FileWriter(countsf);
		 for(int a=0;a<mBigramCounts.length;a++)
		 {
			 for(int b=0;b<mBigramCounts[0].length;b++)
			 {
				 if(mBigramCounts[a][b]!=0)
				 {
				 countWriter.write("\r\n" + a +","+b+":"+mBigramCounts[a][b]);
				 }
				 
			 }
		 }
		 countWriter.close();
		
	}
	
	
	
	/*
	 * @pre: fileName is a legal file path
	 */
	public void loadModel(String fileName) throws IOException
	{ 
		File vocf=new File(fileName+VOC_FILE_SUFFIX );
		File countsf=new File(fileName+COUNTS_FILE_SUFFIX);
		
		FileReader vocfr=new FileReader(vocf);
		FileReader countsfr=new FileReader(countsf);
		
		BufferedReader vocRe=new BufferedReader(vocfr);
		BufferedReader countRe=new BufferedReader(countsfr);
		
		String wordsa=vocRe.readLine();
		String [] readmat=wordsa.split(" ");
		
		int x=Integer.parseInt(readmat[0]);
		String[] vocstr=new String[x];
		String[] voc=new String[x];
		String Curr;
		
		while((Curr=vocRe.readLine())!=null)
		{
			vocstr=Curr.split(" ");
			int a=Integer.parseInt(vocstr[0]);
			voc[a]=vocstr[1];
		}
		vocRe.close();
		mVocabulary=voc;
		
		int [][]mat=new int[x][x];
		String N;
		String [] arr1=new String[2];
		String []arr2=new String[2];
		
		while((N=countRe.readLine())!=null)
		{
			arr1=N.split(",");
			arr2=arr1[1].split(":");
			
			int a=Integer.parseInt(arr1[0]);
			int b=Integer.parseInt(arr2[0]);
			mat[a][b]=Integer.parseInt(arr2[1]);
		}
		countRe.close();
		mBigramCounts=mat;
	}

	
	
	/*
	 * @pre: word is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: word is in lowercase
	 * @post: $ret = -1 if word is not in vocabulary, otherwise $ret = the index of word in vocabulary
	 */
	public int getWordIndex(String word)
	{
		for(int i=0;i<mVocabulary.length;i++)
		{
			if(mVocabulary[i].equals(word))
				return i;
		}
			return ELEMENT_NOT_FOUND;
	}
	
	
	
	/*
	 * @pre: word1, word2 are in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post: $ret = the count for the bigram <word1, word2>. if one of the words does not
	 * exist in the vocabulary, $ret = 0
	 */
	public int getBigramCount(String word1, String word2)
	{ //  Q - 6
		int index1=getWordIndex(word1);
		int index2=getWordIndex(word2);
		
		if(index1!=ELEMENT_NOT_FOUND&&index2!=ELEMENT_NOT_FOUND) 
		{
			return mBigramCounts[index1][index2];
		}
		return 0;
				
	}
	
	
	/*
	 * @pre word in lowercase, and is in mVocabulary
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post $ret = the word with the lowest vocabulary index that appears most fequently after word (if a bigram starting with
	 * word was never seen, $ret will be null
	 */
	public String getMostFrequentProceeding(String word)
	{ //  Q-7
		int indexword=getWordIndex(word);
		int maxc=mBigramCounts[indexword][0];
		int maxword=0;
		for(int j=0;j<mBigramCounts[indexword].length;j++)
		{
			if(mBigramCounts[indexword][j]>maxc)
			{
				maxc=mBigramCounts[indexword][j];
				maxword=j;
			}
		}
		return mVocabulary[maxword];
	}
	
	
	/* @pre: sentence is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: each two words in the sentence are are separated with a single space
	 * @post: if sentence is is probable, according to the model, $ret = true, else, $ret = false
	 */
	public boolean isLegalSentence(String sentence)
	{  //  Q - 8
		String[]sentence_words=sentence.split(" ");
		if(sentence_words.length<=1) return true;
		String word1=sentence_words[0];
		for(int i=1;i<sentence_words.length;i++)
		{
			String word2=sentence_words[i];
			if(getBigramCount(word1,word2)==0)
				return false;
			word1=word2;
		}
		return true;
	}
	
	
	
	/*
	 * @pre: arr1.length = arr2.legnth
	 * post if arr1 or arr2 are only filled with zeros, $ret = -1, otherwise calcluates CosineSim
	 */
	public static double calcCosineSim(int[] arr1, int[] arr2)
	{ 
		if(zeros_check(arr1)||zeros_check(arr2))
		{
			return -1;
		}
		
		int n=arr1.length;
		int up=0;
		int res;
		
		for(int i=0;i<n;i++)
		{
			int a1=arr1[i];
			int a2=arr2[i];
			
			res=a1*a2;
			
			up=up+res;
		}
		
		double down=0;
		double A_down=0;
		double B_down=0;
		
		double Adown_pow;
		double Bdown_pow;
		
		for(int j=0;j<n;j++)
		{
			Adown_pow=Math.pow(arr1[j], 2);
			Bdown_pow=Math.pow(arr2[j], 2);
			
			A_down=A_down+Adown_pow;
			B_down=B_down+Bdown_pow;
		}
		
		double asq=Math.sqrt(A_down);
		double bsq=Math.sqrt(B_down);
		down=(asq)*(bsq);
		return up/down;
	}
	
	public static boolean  zeros_check(int[]arr)
	{
		for(int i=0;i<arr.length;i++)
		{
			if (arr[i]!=0)
				return false;
		}
		return true;
	}

	
	/*
	 * @pre: word is in vocabulary
	 * @pre: the method initModel was called (the language model is initialized), 
	 * @post: $ret = w implies that w is the word with the largest cosineSimilarity(vector for word, vector for w) among all the
	 * other words in vocabulary
	 */
	public String getClosestWord(String word)
	{ //  Q - 10
		int n=mVocabulary.length;
		System.out.println(n);
		int []curr=vector_mat(word);
		int wordind=getWordIndex(word);
		
		int bi=0;
		while(bi==wordind)
			bi++;

		int[] sec_vec=vector_mat(mVocabulary[bi]);
		double result=calcCosineSim(sec_vec,curr);
		
		int min_index=bi;
		double min_vec=result;
		
		for(int i=0;i<n;i++)
		{
			if(i==wordind||i==bi)
				continue;
			else
			{
				sec_vec=vector_mat(word);
				result=calcCosineSim(curr,sec_vec);
				if(result<min_vec)
				{
					min_index=i;
					min_vec=result;
				}
					
			}	
		}
		return mVocabulary[min_index];
	}
	
	public int[] vector_mat(String word)
	{
		int [] vector_res=new int[mVocabulary.length];
		for(int i=0;i<mVocabulary.length;i++)
		{
			vector_res[i]=getBigramCount(word,mVocabulary[i]);
		}
		
		return vector_res;
	}
	
	public int first_index(String word)
	{
		int a=getWordIndex(word);
		int i=0;
		while(i<mVocabulary.length)
		{
			if(i!=a)
				return i;
			i++;
		}
		return i;
	}
	
}
