package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class ServicioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Servicio> getServicios(String value) {
		List<Servicio> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Servicio.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Servicio>) query.getResultList();
		return resultado;
	}

}
