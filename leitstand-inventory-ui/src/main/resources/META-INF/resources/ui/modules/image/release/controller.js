import {Images} from '/ui/modules/image/image.js'
import {Releases,Release} from './release.js';
import {Controller,Menu} from '/ui/js/ui.js';
import {Select} from '/ui/js/ui-components.js';


class ReleaseImageSelector extends Select {
    
    options(){
        const images = new Images({'scope':'_versions'});
        return images.load({'image_type':'onl-installer'})
                     .then(versions => [{'value':''}].concat(versions.map(version => ({'value':version})))); 
    }
    
}
customElements.define('release-image-version',ReleaseImageSelector);


const releasesController = function(){
    const releases = new Releases();
    return new Controller({
        resource:releases,
        viewModel:function(releases){
            return {'filter':this.location.param('filter'),
                    'releases':releases};
        }
    })
};

const releaseController = function(){
    const release = new Release();
    return new Controller({
        resource:release,
        buttons:{
            'save-release':function(){
                release.saveSettings(this.location.params, this.getViewModel());
            },
            'remove-release':function(){
                release.removeRelease(this.location.params);
            }
        },
        onRemoved:function(){
            this.navigate('releases.html');
        }
    })
};

const addReleaseController = function(){
    const releases = new Releases();
    return new Controller({
        resource:releases,
        viewModel:function(){
            return {};
        },
        buttons:{
            'add-release':function(){
                releases.addRelease(this.location.params,this.getViewModel());
            }
        },
        selections:{
            'image_version':function(version){
                const images = new Images();
                if(version){
                    images.load({'image_version':version,
                                'image_type':'onl-installer'})
                          .then(images => {
                              // Inject images to the view model 
                              this.updateViewModel({'images':images});
                              // and render view again.
                              this.renderView(); 
                          });                    
                } else {
                    // Inject images to the view model 
                    this.updateViewModel({'images':null});
                    // and render view again.
                    this.renderView(); 
                }

                
            }
        },
        onSuccess:function(){
            this.navigate('releases.html');
        }
    });
};

const releases = {
    'master': releasesController(),
    'details': {
        'new-release.html':addReleaseController(),
        'release.html':releaseController(),
        'confirm-remove.html':releaseController()
    }
};


export const menu = new Menu({'releases.html' : releases});
