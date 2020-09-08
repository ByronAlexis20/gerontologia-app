package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class ActividadDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Actividad> getActividades(String value,Integer idPeriodo) {
		List<Actividad> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Actividad.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Actividad>) query.getResultList();
		return resultado;
	}
}
