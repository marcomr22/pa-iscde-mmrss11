package pt.iscte.pidesco.refactoring;
import java.util.ArrayList;
import java.util.List;

public class Matrix <C,M>{

	private int rows;
	private int columns;

	private List<C> c_map = new ArrayList<C>();
	private List<M> m_map = new ArrayList<M>();
	List<C> final_c = new ArrayList<C>();
	List<M> final_m = new ArrayList<M>();
	private int[][] matrix;

	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.matrix = new int[columns][rows];
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				matrix[i][j] = 0;
			}
		}
	}

	public void addC(C c) {
		if(c_map.contains(c)) {
			return;
		}
		c_map.add(c_map.size(),c);
	}

	public void addM(M m) {
		if(m_map.contains(m)) {
			return;
		}
		m_map.add(m_map.size(),m);
		
	}

	public void setValue(C c, M m, int value) {
		assert(c_map.contains(c));
		assert(m_map.contains(m));
		int row = c_map.indexOf(c);
		int column = m_map.indexOf(m);
		matrix[row][column] = value; 
	}

	public List<C> getFinalC() {
		return this.final_c;
	}

	public List<M> getFinalM() {
		return this.final_m;
	}

	//	public void getSugestion() {
	//		List<C> c = new ArrayList<C>();
	//		List<M> m = new ArrayList<M>();
	//		for (int i = 0; i < columns; i++) {
	//			for (int j = 0; j < rows; j++) {
	//				if(matrix[i][j] == 1 && !m.contains(m_map.get(j))) {
	//					c.add((C) c_map.get(j));
	//				}
	//				else if(matrix[i][j] == 0 && m.contains(m_map.get(j))) {
	//					c.remove(m_map.get(j));
	//				}
	//			}
	//		}
	//		this.final_c = c;
	//		this.final_m = m;
	//	}
	
	//retorna um vetor com os valores a 1 em comum
	public int[] compute(int[] v1, int[] v2) {
		int[] v3 = new int[v1.length];
		for (int i = 0; i < v1.length; i++) {
			if(v1[i] == 1 && v2[i]== 1) {
				v3[i] = 1;
			}
		}
		return v3;
	}

	
	//retorna qual o vetor com maior numero de 1's
	public int[] bigger(int[] v1, int[] v2) {
		int c1 = 0;
		int c2 = 0;
		for (int i = 0; i < v1.length; i++) {
			if(v1[i] == 1) {
				c1++;
			}
			if(v2[i] == 1) {
				c2++;
			}
		}
		if(c1 >= c2) {
			return v1;
		}
		else {
			return v2;
		}
	}

	//retorna um vetor com a sugestão de quais M ocorrem mais
	public int[] getSugestion() {

		List<int[]> lista = new ArrayList<>();
		for (int i = 0; i < columns - 1; i++) {
			lista.add(compute(matrix[i],matrix[i+1]));
		}
		int[] l = lista.get(0);
		for (int i = 0; i < lista.size() - 1; i++) {
			l = bigger(l, lista.get(i+1));
		}
		return l;
	}

	//retorna os M e os C que tem mais ocorrencia
	public void choose(int[] l) {
		List<C> c = new ArrayList<C>();
		List<M> m = new ArrayList<M>();
		for (int i = 0; i < l.length; i++) {
			if(l[i] == 1 && !m.contains(m_map.get(i))) {
				m.add(m_map.get(i));
				for(int j = 0; j < columns; j++) {
					if(matrix[i][j] == 1 && !c.contains(c_map.get(i))) {
						c.add(c_map.get(i));
					}
				}
			}
		}
		this.final_c = c;
		this.final_m = m;
	}

	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < columns; i++) {
			s += "\n";
			for (int j = 0; j < rows; j++) {
				s += matrix[i][j];
			}
		}
		return s;
	}
}