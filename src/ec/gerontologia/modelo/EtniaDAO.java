package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class EtniaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Etnia> getEtnias(String value) {
		List<Etnia> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Etnia.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Etnia>) query.getResultList();
		return resultado;
	}
}
