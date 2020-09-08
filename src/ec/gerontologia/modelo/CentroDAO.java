package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class CentroDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Centro> getCentros(String value) {
		List<Centro> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Centro.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Centro>) query.getResultList();
		return resultado;
	}

}
