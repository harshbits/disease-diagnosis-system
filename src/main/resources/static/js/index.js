var index = angular.module('indexPage', [ 'ngMaterial', 'ngMessages', 'md.data.table']);


index.run(function($templateCache, $http) {
    $http.get('source_files/retrieve-progress-bar.html').then(function(response) {
        $templateCache.put('retrieve-progress-bar', response.data);
    });
});


index.factory('diseaseRetrieve', function($http) {
	return {
		getRetrievedData : function(symptoms) {
			return $http.get('disease/', {
				transformRequest : angular.identity,
				params : {
					symptoms : symptoms,
				}
			});
		}
	}
});

index.factory('diseaseInfoRetrieve', function($http) {
	return {
		getRetrievedData : function(diseaseName) {
			return $http.get('diseaseinfo/', {
				transformRequest : angular.identity,
				params : {
					disease : diseaseName,
				}
			});
		}
	}
});


index.controller('IndexController', ['$scope','$filter', '$mdDialog', 'diseaseRetrieve', 'diseaseInfoRetrieve', '$templateCache',
                                     function($scope, $filter, $mdDialog, diseaseRetrieve, diseaseInfoRetrieve, $templateCache, $timeout, $q){
	
	$scope.states        = loadAll();
	$scope.querySearch   = querySearch;
	$scope.searchTextChange   = searchTextChange;
	$scope.symptomsSelected = [];
	$scope.newState = newState;
	$scope.options = {
			rowSelection : true,
			multiSelect : true,
			autoSelect : true,
			decapitate : false,
			largeEditDialog : false,
			boundaryLinks : false,
			limitSelect : true,
			pageSelect : true
		};
	

    function newState(state) {
      alert("Sorry! You'll need to create a Constitution for " + state + " first!");
    }

    function querySearch (query) {
      var results = query ? $scope.states.filter( createFilterFor(query) ) : $scope.states,
          deferred;
      if ($scope.simulateQuery) {
        deferred = $q.defer();
        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
        return deferred.promise;
      } else {
        return results;
      }
    }

    function searchTextChange(text) {
    	console.log('Text changed to ' + text);
    }
    
    
    $scope.selectedItemChange = function(item) {
    	console.log('Item changed to ' + JSON.stringify(item));
        if(item)
        {
          //check if item is already selected
          if($filter('filter')($scope.symptomsSelected, function (symptom) {return symptom === item.display;})[0])
            {
        	  console.log('Item already selected. Will not add it again.');
            }
          else
            {
              //add id to object
        	  $scope.symptomsSelected.push(item.display);    
            }
          // clear search field
          $scope.searchText = '';
          $scope.selectedItem = undefined;
          
          //somehow blur the autocomplete focus
          //$mdAutocompleteCtrl.blur();
          document.getElementById("symptomAc").blur();
        }
      };
    
    $scope.symptomsSelectedRemove = function(symptom){
    	var index = $scope.symptomsSelected.indexOf(symptom);
    	$scope.symptomsSelected.splice(index, 1);   
    	
    };
    
    /**
     * Build `states` list of key/value pairs
     */
    function loadAll() {
    
    	var allStates = 'Chest Pain, Cough, Diarrhea, Dizziness, Eye discomfort and redness, Fast Heart Beats, Fever, Forehead Pain, Headache Pain, Headaches, Heart palpitations, High Body Temperature, Low Body Temperature, Muscles Pain, Neck pain, Normal Body Temperature, Normal Heart Beats, Numbness or tingling in hands, Running Nose, Shortness of Breath, Shoulder pain, Slow Heart Beats, Sore throat, Urinary problems, Vision problems, Vomiting and Nausea, Watery Eyes, Wheezing';

      return allStates.split(/, +/g).map( function (state) {
        return {
          value: state.toLowerCase(),
          display: state
        };
      });
    }

    /**
     * Create filter function for a query string
     */
    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);

      return function filterFn(state) {
        return (state.value.indexOf(lowercaseQuery) === 0);
      };

    };
    
    $scope.submit = function(){
    	
    	var promise = diseaseRetrieve.getRetrievedData($scope.symptomsSelected);
    	$scope.showRetrieveProgress(true, "", false);
		promise.then(
				function(response) {
				console.log(response);
				$scope.disease = response.data.disesae;
				$scope.hospital = response.data.hospital;
				$scope.hospitalUrl = 'http://maps.google.com/?q='+$scope.hospital;
				$scope.risk = response.data.risk;
				$scope.showRetrieveProgress(
						false,
						"Retrieved all data.",
						false);
				},
				function(response) {
					console.log(response);
					$scope.showRetrieveProgress(
							false,
							"Retrieved all data.",
							false);
		
			});
    }
    
 $scope.search = function(disease){
    	var promise = diseaseInfoRetrieve.getRetrievedData(disease);
    	$scope.showRetrieveProgress(true, "", false);
		promise.then(
				function(response) {
					console.log(response);
					$scope.name = response.data.name;
					$scope.doid = response.data.doid;
					$scope.definition = response.data.definition;
					$scope.links = response.data.links;
					$scope.Xrefs = response.data.Xrefs;
					$scope.synonyms = response.data.synonyms;
					
					$scope.showRetrieveProgress(
							false,
							"Retrieved all data.",
							false);
				},
				function(response) {
					console.log(response);
					$scope
					.showRetrieveProgress(
							false,
							"Retrieved all data.",
							false);
		
			});
    };
    
    $scope.showRetrieveProgress = function(value,
			retrieveMessage, isError) {
		if ((value == true)
				|| (value == false && isError == true)) {
			$mdDialog
					.show({
						clickOutsideToClose : false,
						preserveScope : true,
						template : $templateCache.get('retrieve-progress-bar'),
						locals : {
							value : value,
							retrieveMessage : retrieveMessage
						},
						controller : function DialogController(
								$scope, $mdDialog,
								value, retrieveMessage) {

							$scope.value = value;
							$scope.retrieveMessage = retrieveMessage;
							$scope.cancel = function() {
								$mdDialog.cancel();
							};
						}
					});
		} else {
			$mdDialog.cancel();
		}
	};
		
}]);
