package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class DiscapacidadDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Discapacidad> getDiscapacidades(String value) {
		List<Discapacidad> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Discapacidad.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Discapacidad>) query.getResultList();
		return resultado;
	}

}
