import {Resource} from '/ui/js/client.js'

export class Releases extends Resource {
    
    load(params){
        return this.json('/api/v1/releases?filter={{filter}}',params)
                   .GET();
    }
    
    addRelease(params,release){
        return this.json('/api/v1/releases')
                   .POST(release);
    }
    
}

export class Release extends Resource {
    
    constructor(cfg){
        super();
        this._cfg = cfg;
    }
    
    load(params){
        if(this._cfg && this._cfg.scope == '_status'){
            return this.json('/api/v1/releases/{{release}}/_status',params)
                       .GET();
        }
        
        return this.json('/api/v1/releases/{{release}}',params)
                   .GET();
    }
    
    saveSettings(params,release){
        return this.json('/api/v1/releases/{{release}}',params)
                   .PUT(release);
    }
    
    removeRelease(params){
        return this.json('/api/v1/releases/{{release}}',params)
                   .DELETE();
    }

}

