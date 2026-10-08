<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Nouveau Patient - Infirmier" />
</jsp:include>

<div class="max-w-4xl mx-auto space-y-6">
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center justify-between">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">US1 &bull; Étape 2b : Enregistrement d'un nouveau patient</h1>
            <p class="mt-1 text-sm text-slate-500">
                Saisie administrative, médicale et prise des signes vitaux initiaux
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/infirmier/recherche" class="text-sm font-medium text-slate-500 hover:text-slate-800">
            <i class="fa-solid fa-arrow-left mr-1"></i> Retour à la recherche
        </a>
    </div>

    <c:if test="${not empty error}">
        <div class="p-4 rounded-xl bg-red-50 border border-red-200 text-red-700 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-circle-exclamation text-red-500"></i>
            <span>${error}</span>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/infirmier/nouveau" method="POST" class="space-y-6">
        <input type="hidden" name="csrfToken" value="${csrfToken}" />

        <!-- Section 1: Données administratives -->
        <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
            <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                <i class="fa-solid fa-id-card text-sky-600 mr-2"></i> Données Administratives
            </h2>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Nom *</label>
                    <input type="text" name="nom" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="ex: El Fassi" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Prénom *</label>
                    <input type="text" name="prenom" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="ex: Mehdi" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Date de naissance *</label>
                    <input type="date" name="dateNaissance" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">N° Sécurité Sociale *</label>
                    <input type="text" name="numSecu" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500 font-mono" placeholder="ex: GH987654" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Téléphone</label>
                    <input type="tel" name="telephone" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="06..." />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Mutuelle</label>
                    <input type="text" name="mutuelle" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="CNSS, CNOPS, etc." />
                </div>
                <div class="sm:col-span-2">
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Adresse (optionnel)</label>
                    <input type="text" name="adresse" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="Adresse complète..." />
                </div>
            </div>
        </div>

        <!-- Section 2: Données médicales (antécédents, allergies, traitements) -->
        <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
            <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                <i class="fa-solid fa-notes-medical text-sky-600 mr-2"></i> Recueil des Données Médicales
            </h2>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Antécédents</label>
                    <textarea name="antecedents" rows="3" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="Pathologies antérieures, chirurgies..."></textarea>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Allergies</label>
                    <textarea name="allergies" rows="3" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="Pénicilline, pollens..."></textarea>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Traitements en cours</label>
                    <textarea name="traitements" rows="3" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" placeholder="Médicaments actuels..."></textarea>
                </div>
            </div>
        </div>

        <!-- Section 3: Mesure des signes vitaux -->
        <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
            <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                <i class="fa-solid fa-heart-pulse text-sky-600 mr-2"></i> Mesure des Signes Vitaux à l'arrivée
            </h2>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Tension artérielle *</label>
                    <input type="text" name="tension" required placeholder="ex: 120/80" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">mmHg</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Fréquence cardiaque *</label>
                    <input type="number" name="frequenceCardiaque" required min="30" max="250" placeholder="ex: 75" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">bpm (battements/min)</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Température corporelle *</label>
                    <input type="number" step="0.1" name="temperature" required min="30" max="45" placeholder="ex: 37.0" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">°C</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Fréquence respiratoire *</label>
                    <input type="number" name="frequenceRespiratoire" required min="5" max="60" placeholder="ex: 16" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">cycles/min</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Poids (si nécessaire)</label>
                    <input type="number" step="0.1" name="poids" min="1" max="300" placeholder="ex: 72.5" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">kg</span>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Taille (si nécessaire)</label>
                    <input type="number" step="0.5" name="taille" min="30" max="250" placeholder="ex: 175" class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                    <span class="text-xs text-slate-400">cm</span>
                </div>
            </div>
        </div>

        <div class="flex justify-end space-x-3 pt-2">
            <a href="${pageContext.request.contextPath}/infirmier/patients"
               class="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-semibold transition">
                Annuler
            </a>
            <button type="submit"
                    class="px-6 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-sm font-semibold shadow-md transition flex items-center space-x-2">
                <i class="fa-solid fa-check"></i>
                <span>Créer le dossier & Intégrer à la file d'attente</span>
            </button>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
