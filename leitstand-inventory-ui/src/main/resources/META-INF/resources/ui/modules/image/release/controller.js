import {Images} from '/ui/modules/image/image.js'
import {Releases,Release} from './release.js';
import {Controller,Menu} from '/ui/js/ui.js';
import './components.js';

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
                releases.addRelease(this.location.params, this.getViewModel());
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
