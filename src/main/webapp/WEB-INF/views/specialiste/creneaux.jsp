<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Mes Créneaux Horaires - Médecin Spécialiste" />
</jsp:include>

<div class="space-y-6">
    <div class="sm:flex sm:items-center sm:justify-between bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">US6 : Gestion de mes créneaux horaires</h1>
            <p class="mt-1 text-sm text-slate-500">
                Créneaux fixes prédéfinis de 30 min &bull; Mise à jour en temps réel (Disponible / Indisponible / Archivé)
            </p>
        </div>
        <form action="${pageContext.request.contextPath}/specialiste/creneaux" method="GET" class="mt-4 sm:mt-0 flex items-center space-x-2">
            <label class="text-xs font-semibold text-slate-600">Date :</label>
            <input type="date" name="date" value="${selectedDate}"
                   class="px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-purple-500" />
            <button type="submit" class="px-4 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-xl text-sm font-semibold transition">
                Changer
            </button>
        </form>
    </div>

    <!-- Slots Table -->
    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div class="p-4 bg-slate-50 border-b border-slate-200 flex items-center justify-between">
            <span class="text-sm font-bold text-slate-800">
                <i class="fa-solid fa-calendar-day mr-1 text-purple-600"></i> Programme du : ${selectedDate}
            </span>
            <span class="text-xs text-slate-500">Durée par consultation : 30 minutes</span>
        </div>

        <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200 text-left text-sm">
                <thead class="bg-slate-50/50 text-slate-500 uppercase font-semibold text-xs tracking-wider">
                    <tr>
                        <th class="px-6 py-4">Créneau</th>
                        <th class="px-6 py-4">Statut</th>
                        <th class="px-6 py-4 text-right">Action (Disponibilité)</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-slate-100 bg-white">
                    <c:forEach var="cr" items="${creneaux}">
                        <tr class="hover:bg-slate-50/80 transition">
                            <td class="px-6 py-4 whitespace-nowrap font-bold text-slate-900">
                                <i class="fa-regular fa-clock mr-2 text-purple-600"></i>
                                ${cr.plageHoraire}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span class="px-3 py-1 rounded-full text-xs font-bold
                                    <c:choose>
                                        <c:when test="${cr.statut == 'DISPONIBLE'}">bg-emerald-100 text-emerald-800 border border-emerald-200</c:when>
                                        <c:when test="${cr.statut == 'INDISPONIBLE'}">bg-red-100 text-red-800 border border-red-200</c:when>
                                        <c:otherwise>bg-slate-100 text-slate-600 border border-slate-200</c:otherwise>
                                    </c:choose>">
                                    ${cr.statut.libelle}
                                </span>
                            </td>
                            <td class="px-6 py-4 text-right whitespace-nowrap">
                                <c:choose>
                                    <c:when test="${cr.statut == 'ARCHIVE'}">
                                        <span class="text-xs text-slate-400 italic">Créneau passé</span>
                                    </c:when>
                                    <c:when test="${cr.statut == 'DISPONIBLE'}">
                                        <form action="${pageContext.request.contextPath}/specialiste/creneaux" method="POST" class="inline">
                                            <input type="hidden" name="csrfToken" value="${csrfToken}" />
                                            <input type="hidden" name="creneauId" value="${cr.id}" />
                                            <input type="hidden" name="statut" value="INDISPONIBLE" />
                                            <input type="hidden" name="date" value="${selectedDate}" />
                                            <button type="submit" class="px-3 py-1.5 bg-red-50 hover:bg-red-100 text-red-700 rounded-lg text-xs font-semibold border border-red-200 transition">
                                                Rendre indisponible
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/specialiste/creneaux" method="POST" class="inline">
                                            <input type="hidden" name="csrfToken" value="${csrfToken}" />
                                            <input type="hidden" name="creneauId" value="${cr.id}" />
                                            <input type="hidden" name="statut" value="DISPONIBLE" />
                                            <input type="hidden" name="date" value="${selectedDate}" />
                                            <button type="submit" class="px-3 py-1.5 bg-emerald-50 hover:bg-emerald-100 text-emerald-700 rounded-lg text-xs font-semibold border border-emerald-200 transition">
                                                Rendre disponible (Annulation)
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
