package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class SaludDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Salud> getEstadosSalud() {
		List<Salud> resultado; 
		Query query = getEntityManager().createNamedQuery("Salud.buscarSalud");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Salud>) query.getResultList();
		return resultado;
	}
}
